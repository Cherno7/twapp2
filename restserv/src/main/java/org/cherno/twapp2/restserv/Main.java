package org.cherno.twapp2.restserv;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.cherno.twapp2.restserv.sdargs.ConfigurationArgs;
import org.cherno.twapp2.service.TwappServiceImpl;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;

/**
 * Created on 08.06.2015.
 */
public class Main {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    private static HttpServer startServer(Configuration externalConfiguration, String uri) {
        final ResourceConfig rc = new ResourceConfig().packages("org.cherno.twapp2.restserv")
            .register(createMoxyJsonResolver())
            .property("service", new TwappServiceImpl(externalConfiguration));

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        installSLF4JBridge();

        Configuration externalConfiguration = null;
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();

        final ConfigurationArgs configurationArgs = new ConfigurationArgs();
        JCommander jCommander = new JCommander(configurationArgs);

        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            logger.error("{}", e.getMessage());
            jCommander.usage();
            return;
        }

        try {
            externalConfiguration = new PropertiesConfiguration(configurationArgs.getConfigurationFile());
            compositeConfiguration.addConfiguration(externalConfiguration);
            compositeConfiguration.addConfiguration(new PropertiesConfiguration("restserv.properties"));
        } catch (ConfigurationException e) {
            logger.error("{}", e.getMessage());
        }

        startServer(externalConfiguration, compositeConfiguration.getString("restserv.URI"));
    }


    private static void installSLF4JBridge() {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
}

