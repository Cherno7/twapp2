package org.cherno.twapp2.restserv;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jersey2.InstrumentedResourceMethodApplicationListener;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.cherno.twapp2.restserv.sdargs.ConfigurationArgs;
import org.cherno.twapp2.service.TwappService;
import org.cherno.twapp2.service.TwappServiceImpl;
import org.cherno.twapp2.service.city.CityChecker;
import org.cherno.twapp2.service.city.CityCheckerGeoNames;
import org.cherno.twapp2.service.country.CountryChecker;
import org.cherno.twapp2.service.country.CountryCheckerISO3166;
import org.cherno.twapp2.service.storage.MemoryStorage;
import org.cherno.twapp2.service.storage.Storage;
import org.cherno.twapp2.twitterDAO.TwappDAO;
import org.cherno.twapp2.twitterDAO.TwappDAOImpl;
import org.cherno.twapp2.twitterDAO.cache.TwappCache;
import org.cherno.twapp2.twitterDAO.cache.TwappNoCacheImpl;
import org.cherno.twapp2.twitterDAO.http.CachingTwitterHttpClient;
import org.cherno.twapp2.twitterDAO.http.TwitterHttpClient;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;

/**
 * Created on 08.06.2015.
 */
public class Main {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
    private static final MetricRegistry metrics = new MetricRegistry();

    private static TwappCache twappCache;
    private static TwitterHttpClient twitterHttpClient;
    private static TwappDAO twappDAO;
    private static CityChecker cityChecker;
    private static CountryChecker countryChecker;
    private static Storage storage;
    private static TwappService twappService;
    private static CompositeConfiguration configuration;

    private static HttpServer startServer(String uri) {
        final ResourceConfig rc = new ResourceConfig().packages("org.cherno.twapp2.restserv")
                .register(createMoxyJsonResolver())
                .register(new InstrumentedResourceMethodApplicationListener(metrics))
                .property("service", twappService)
                .property("metricregistry", metrics);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
    }

    public static void main(String[] args) throws IOException {
        // installing bridge
        installSLF4JBridge();

        //parsing args
        final ConfigurationArgs configurationArgs = new ConfigurationArgs();
        JCommander jCommander = new JCommander(configurationArgs);
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            logger.error("{}", e.getMessage());
            jCommander.usage();
            return;
        }

        //loading configuration
        try {
            loadConfiguration(configurationArgs);
        } catch (ConfigurationException e) {
            logger.error("Configuration error. {}", e.getMessage());
            return;
        }

        //creatind deps and starting server
        createDependencies();
        startServer(configuration.getString("restserv.URI"));
    }

    private static void createDependencies() {
        try {
            Class c = Class.forName(configuration.getString("twitterdao.cache"));
            Constructor construct =  c.getConstructor(Configuration.class);
            twappCache  = (TwappCache) construct.newInstance(configuration);
        } catch (ReflectiveOperationException e) {
            logger.error("Cache implementation {} not found. Caching disabled", configuration.getString("twitterdao.cache"));
            twappCache = new TwappNoCacheImpl();
        }

        twitterHttpClient = new CachingTwitterHttpClient(configuration, twappCache);
        twappDAO = new TwappDAOImpl(configuration, twitterHttpClient);
        cityChecker = new CityCheckerGeoNames(configuration);
        countryChecker = new CountryCheckerISO3166();
        storage = MemoryStorage.getInstance("twapp");
        twappService = new TwappServiceImpl(configuration, twappDAO, storage, countryChecker, cityChecker);
    }

    private static void loadConfiguration(ConfigurationArgs configurationArgs) throws ConfigurationException{
        Configuration externalConfiguration = null;
        configuration = new CompositeConfiguration();

        externalConfiguration = new PropertiesConfiguration(configurationArgs.getConfigurationFile());
        configuration.addConfiguration(externalConfiguration);
        configuration.addConfiguration(new PropertiesConfiguration("restserv.properties"));

        if (!configuration.containsKey("twitterdao.consumerKey") || !configuration.containsKey("twitterdao.consumerSecret") ||
            !configuration.containsKey("twitterdao.accessToken") || !configuration.containsKey("twitterdao.accessTokenSecret")) {
            throw new ConfigurationException("Invalid configuration.");
        }
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
