package com.sunft.chapter05;

/**
 * 单例模式的懒加载形式
 */
public class LazySingleton {

    private LazySingleton() {
        System.out.println("LazySingleton is create");
    }

    private static LazySingleton instance = null;

    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }

}
