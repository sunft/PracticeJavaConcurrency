package com.sunft.chapter06;

public interface IHourse {

    void eat();

    default void run() {
        System.out.println("hourse run");
    }

}
