package com.sunft.chapter06;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * newKeySet()方法返回一个线程安全的Set
 */
public class NewKeySetDemo {

    public static Set<String> newKeySet() {
        return ConcurrentHashMap.newKeySet();
    }
}
