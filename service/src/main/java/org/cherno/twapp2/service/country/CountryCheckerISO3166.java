package org.cherno.twapp2.service.country;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 08.07.2015.
 */
public class CountryCheckerISO3166 implements CountryChecker {
    private Map<String, String> countries;

    public CountryCheckerISO3166() {
        this.countries = Collections.unmodifiableMap(getCountryList());
    }

    private Map<String, String> getCountryList() {
        Map <String, String> result = new HashMap<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale loc = new Locale("", countryCode);
            String code = loc.getCountry();
            for(Locale locale : Locale.getAvailableLocales()) {
                result.put(loc.getDisplayCountry(locale).toLowerCase(), code);
            }
        }
        return result;
    }

    public boolean isCountryCode(String code) {
        return countries.containsValue(code);
    }

    public boolean isCountry(String name) {
        return countries.containsKey(name);
    }

    public String getCountryCode(String name) {
        return countries.get(name);
    }
}
