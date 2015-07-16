package org.cherno.twapp2.service;

import org.apache.commons.configuration.Configuration;
import org.cherno.twapp2.service.city.CityChecker;
import org.cherno.twapp2.service.country.CountryChecker;
import org.cherno.twapp2.service.storage.Storage;
import org.cherno.twapp2.twitterDAO.TwappDAO;
import org.cherno.twapp2.twitterDAO.TwappData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TwappServiceImplTest {

    TwappService twappService;
    @Mock
    TwappDAO twappDAO;
    @Mock
    Storage storage;
    @Mock
    Configuration configuration;
    @Mock
    CityChecker cityChecker;
    @Mock
    CountryChecker countryChecker;

    @Before
    public void setUp() throws Exception {

        //TwappData with a partial data
        TwappData twappDataPartial = new TwappData();
        twappDataPartial.setFriendsRemainingLimit(0);
        twappDataPartial.setFollowersRemainingLimit(10);
        twappDataPartial.setFriendsResponseStatus(429);
        twappDataPartial.setFollowersResponseStatus(200);
        twappDataPartial.setFollowersLocations(Arrays.asList("Russia, Omsk", "Omsk, Russia", "Russia, ufA", "uFa", "Ufa", "Amsterdam", ""));

        when(twappDAO.getTwitterData("testPartial")).thenReturn(twappDataPartial);

        TwappData twappDataFull = new TwappData();
        twappDataFull.setFriendsRemainingLimit(10);
        twappDataFull.setFollowersRemainingLimit(10);
        twappDataFull.setFriendsResponseStatus(200);
        twappDataFull.setFollowersResponseStatus(200);
        twappDataFull.setFollowersLocations(Arrays.asList("Russia, Omsk", "Omsk, Russia", "Russia, ufA", "uFa", "Ufa", "Amsterdam", ""));
        twappDataFull.setFriendsLocations(Arrays.asList("", "Ukraine, Kiev", "Omsk", "Omsk"));

        when(twappDAO.getTwitterData("testFull")).thenReturn(twappDataFull);

        when(countryChecker.isCountry("russia")).thenReturn(true);
        when(countryChecker.isCountry("ukraine")).thenReturn(true);
        when(countryChecker.getCountryCode("russia")).thenReturn("RU");
        when(countryChecker.getCountryCode("ukraine")).thenReturn("UA");

        when(cityChecker.isCity("omsk")).thenReturn(true);
        when(cityChecker.isCity("ufa")).thenReturn(true);
        when(cityChecker.isCity("kiev")).thenReturn(true);
        when(cityChecker.isCity("amsterdam")).thenReturn(true);

        when(cityChecker.getCityName("omsk")).thenReturn("omsk");
        when(cityChecker.getCityName("ufa")).thenReturn("ufa");
        when(cityChecker.getCityName("kiev")).thenReturn("kiev");
        when(cityChecker.getCityName("amsterdam")).thenReturn("amsterdam");


        twappService = new TwappServiceImpl(configuration, twappDAO, storage, countryChecker, cityChecker);
    }

    @Test
    public void testGetSuggestedLocationPartial() throws Exception {
        SuggestedLocationModel suggestedLocationModel = twappService.getSuggestedLocation("testPartial", true);
        assertEquals("RU, ufa", suggestedLocationModel.getSuggestedLocation());
    }

    @Test
    public void testGetSuggestedLocationFull() throws Exception {
        SuggestedLocationModel suggestedLocationModel = twappService.getSuggestedLocation("testFull", true);
        assertEquals("RU, omsk", suggestedLocationModel.getSuggestedLocation());
    }

    @Test
    public void testGetLocationsSkipEmpty() throws Exception {
        LocationsModel locationsModel = twappService.getLocations("testFull", true);
        assertEquals(9, locationsModel.getLocations().size());
    }

    @Test
    public void testGetLocationsKeepEmpty() throws Exception {
        LocationsModel locationsModel = twappService.getLocations("testFull", false);
        assertEquals(11, locationsModel.getLocations().size());
    }
}