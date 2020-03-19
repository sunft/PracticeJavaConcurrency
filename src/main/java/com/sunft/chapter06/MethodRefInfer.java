package com.sunft.chapter06;

import java.util.Arrays;

/**
 * 方法引用推导
 */
public class MethodRefInfer {

    static int[] arr = {1, 3, 4, 5, 6, 7, 8, 9, 10};

    public static void main(String[] args) {
        Arrays.stream(arr).forEach(System.out::println);
    }

}
