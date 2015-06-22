package org.cherno.twapp2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 08.06.2015.
 */
public class Util {
    public static <T> T getMostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T element : list) {
            Integer count = map.get(element);
            if (count == null) {
                map.put(element, 1);
            } else {
                map.replace(element, ++count);
            }
        }

        Map.Entry<T, Integer> mostCommon = null;

        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            if (mostCommon == null || entry.getValue() > mostCommon.getValue())
                mostCommon = entry;
        }
        return mostCommon.getKey();
    }
}
