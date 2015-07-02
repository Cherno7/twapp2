package org.cherno.twapp2.service;

import org.cherno.twapp2.twitterDAO.TwappDAO;
import org.cherno.twapp2.twitterDAO.TwappData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TwappServiceImplTest {

    @Mock
    TwappDAO twappDAO;

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


    }

    @Test
    public void testGetSuggestedLocationPartial() throws Exception {
        TwappService twappService = new TwappServiceImpl(twappDAO);
        SuggestedLocationModel suggestedLocationModel = twappService.getSuggestedLocation("testPartial", true);
        assertEquals("russia, ufa", suggestedLocationModel.getSuggestedLocation());
    }

    @Test
    public void testGetSuggestedLocationFull() throws Exception {
        TwappService twappService = new TwappServiceImpl(twappDAO);
        SuggestedLocationModel suggestedLocationModel = twappService.getSuggestedLocation("testFull", true);
        assertEquals("russia, omsk", suggestedLocationModel.getSuggestedLocation());
    }

    @Test
    public void testGetLocationsSkipEmpty() throws Exception {
        TwappService twappService = new TwappServiceImpl(twappDAO);
        LocationsModel locationsModel = twappService.getLocations("testFull", true);
        assertEquals(9, locationsModel.getLocations().size());
    }

    @Test
    public void testGetLocationsKeepEmpty() throws Exception {
        TwappService twappService = new TwappServiceImpl(twappDAO);
        LocationsModel locationsModel = twappService.getLocations("testFull", false);
        assertEquals(11, locationsModel.getLocations().size());
    }
}