package com.sunft.chapter06;

import java.util.Arrays;
import java.util.Random;

/**
 * 测试生成器
 */
public class IntUnaryOperatorDemo {

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random r = new Random();
        Arrays.setAll(arr, (i) -> r.nextInt(100));
        for (int value : arr) {
            System.out.print(value + " ");
        }

        Arrays.parallelSetAll(arr, (i) -> r.nextInt());
    }

}
