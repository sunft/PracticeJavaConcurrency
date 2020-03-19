package com.sunft.chapter06;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 条件插入
 */
public class ConditionInsertion {

    public static class HeavyObject {
        public HeavyObject() {
            System.out.println("HeavyObject created.");
        }
    }

    public static HeavyObject getOrCreate(ConcurrentHashMap<String, HeavyObject> map, String key) {
        HeavyObject value = map.get(key);
        if (value == null) {
            value = new HeavyObject();
            map.put(key, value);
        }
        return value;
    }

    /**
     * 使用HashMap
     *
     * @param map
     * @param key
     * @return
     */
    public static HeavyObject getOrCreateUseCHM(ConcurrentHashMap<String, HeavyObject> map, String key) {
        return map.computeIfAbsent(key, k -> new HeavyObject());
    }

    public static void main(String[] args) {
        ConcurrentHashMap<String, HeavyObject> map = new ConcurrentHashMap<>();
        HeavyObject obj = getOrCreate(map, "1");
    }

}
