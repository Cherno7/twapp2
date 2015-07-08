package org.cherno.twapp2.service.country;

/**
 * Created on 08.07.2015.
 */
public interface CountryChecker {
    public boolean isCountry(String name);
    public String getCountryCode(String name);
}
