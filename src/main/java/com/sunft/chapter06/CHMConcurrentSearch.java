package com.sunft.chapter06;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap的search操作
 */
public class CHMConcurrentSearch {


    public static Integer search(ConcurrentHashMap<String, Integer> map) {
        int found = map.search(2, (s, integer) -> {
            if (integer % 25 == 0) {
                return integer;
            }
            return null;
        });
        return found;
    }

}
