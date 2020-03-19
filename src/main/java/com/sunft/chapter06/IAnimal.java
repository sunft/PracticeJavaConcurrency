package com.sunft.chapter06;

public interface IAnimal {

    default void breath() {
        System.out.println("breath");
    }

}
