package org.cherno.twapp2.service;

import org.apache.commons.configuration.Configuration;
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

    @Mock
    Configuration testConfiguration;

    @Before
    public void setUp() throws Exception {

        //TwappData with a partial data
        TwappData twappData = new TwappData();
        twappData.setFriendsRemainingLimit(0);
        twappData.setFollowersRemainingLimit(10);
        twappData.setFriendsResponseStatus(429);
        twappData.setFollowersResponseStatus(200);
        twappData.setFollowersLocations(Arrays.asList("Russia, Omsk", "Omsk, Russia", "Russia, Ufa", "Ufa", "Ufa", "Amsterdame", ""));

        when(twappDAO.getTwitterData("test", 100)).thenReturn(twappData);
    }

    @Test
    public void testGetSuggestedLocation() throws Exception {
        TwappService twappService = new TwappServiceImpl(testConfiguration, twappDAO);

        SuggestedLocationModel suggestedLocationModel = twappService.getSuggestedLocation("test", 100, true);

        assertEquals("russia, ufa", suggestedLocationModel.getSuggestedLocation());

    }

    @Test
    public void testGetLocations() throws Exception {
    }
}