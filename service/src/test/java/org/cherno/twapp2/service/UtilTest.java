package org.cherno.twapp2.service;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testGetMostCommon() throws Exception {
        final List<String> testList = Arrays.asList("1","2","3","1","1","2","2","1");
        final String result = Util.getMostCommon(testList);
        assertEquals(result, "1");
    }
}