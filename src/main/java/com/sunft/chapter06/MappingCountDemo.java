package com.sunft.chapter06;

import java.util.concurrent.ConcurrentHashMap;

/**
 * MappingCount测试
 */
public class MappingCountDemo {

    public static final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    public static Long mappingCount() {
        return map.mappingCount();
    }

}
