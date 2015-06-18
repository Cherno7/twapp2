package org.cherno.twapp2.restserv;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.cherno.twapp2.restserv.sdargs.ConfigurationArgs;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created on 08.06.2015.
 */
public class Main {
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    private static HttpServer startServer(Configuration externalConfiguration) {
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        compositeConfiguration.addConfiguration(externalConfiguration);
        try {
            compositeConfiguration.addConfiguration(new PropertiesConfiguration(Main.class.getResource("restserv.properties")));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


        final ResourceConfig rc = new ResourceConfig().packages("org.cherno.twapp2.restserv")
                .register(createMoxyJsonResolver());
        rc.property("configuration", externalConfiguration);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(compositeConfiguration.getString("restserv.URI")), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getLogger("global").setLevel(Level.INFO);

        final ConfigurationArgs converterArgs = new ConfigurationArgs();
        JCommander jCommander = new JCommander(converterArgs);

        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            jCommander.usage();
            return;
        }

        Configuration externalConfiguration = null;
        try {
            externalConfiguration = new PropertiesConfiguration(converterArgs.getConfigurationFile());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


        final HttpServer server = startServer(externalConfiguration);
        System.out.println("Server started. Hit enter to stop it...");
        System.in.read();
        server.shutdownNow();
    }
	

    private static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
}

