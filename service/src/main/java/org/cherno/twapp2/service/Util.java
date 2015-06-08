package org.cherno.twapp2.service;

import java.util.Collections;
import java.util.List;

/**
 * Created on 08.06.2015.
 */
public class Util {
    public static <T> T getMostCommon(List<T> list) {
        T result = null;
        int maxCount = 0;
        for(T element : list) {
            int count = Collections.frequency(list, element);
            if (count > maxCount) {
                result = element;
                maxCount = count;
            }
        }
        return result;
    }
}
