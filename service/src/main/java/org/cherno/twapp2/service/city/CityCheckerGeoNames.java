package org.cherno.twapp2.service.city;

import com.opencsv.CSVReader;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 09.07.2015.
 */
public class CityCheckerGeoNames implements CityChecker {
    private static final Logger logger = LoggerFactory.getLogger(CityCheckerGeoNames.class);
    private Configuration configuration =new CompositeConfiguration();

    private Map<Integer, String> canonNames;
    private Map<String, Integer> alternativeNames;

    public CityCheckerGeoNames(Configuration configuration) {
        this.configuration = configuration;
        readMaps();
    }

    private void readMaps() {
        Map <Integer, String> tempCanonNames = new HashMap<>();
        Map <String, Integer> tempAltNames= new HashMap<>();
        CSVReader cityReader;

        try{
            cityReader = new CSVReader(new InputStreamReader(new FileInputStream(this.configuration.getString("service.geofile"))), '\t', '^');
        } catch (IOException e) {
            logger.error("City names file load error. Loading default city list.");
            cityReader = new CSVReader(new InputStreamReader(this.getClass().getResourceAsStream("/cities.csv")), '\t', '^');
        }

        try {
            String[] line;
            while ((line = cityReader.readNext()) != null) {
                tempCanonNames.put(Integer.parseInt(line[0]), line[1].toLowerCase().trim());
                tempAltNames.put(line[1].toLowerCase().trim(), Integer.parseInt(line[0]));
                if(line.length > 3) {
                    for (String str : line[3].split(",")) {
                        tempAltNames.put(str.trim().toLowerCase(), Integer.parseInt(line[0]));
                    }
                }
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
        }


        logger.info("Geonames loaded, total number of cities = {}, alternative names = {} ", tempCanonNames.size(), tempAltNames.size());

        this.canonNames = Collections.unmodifiableMap(tempCanonNames);
        this.alternativeNames = Collections.unmodifiableMap(tempAltNames);
    }

    public boolean isCity(String name) {
        return this.alternativeNames.containsKey(name);
    }

    public String getCityName(String name) {
        return this.canonNames.get(this.alternativeNames.get(name));
    }
}
