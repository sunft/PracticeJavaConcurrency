package com.sunft.chapter06;

/**
 * 驴子接口
 */
public interface IDonkey {

    void eat();

    default void run() {
        System.out.println("Donkey run.");
    }

}
