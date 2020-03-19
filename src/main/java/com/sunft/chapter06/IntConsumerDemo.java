package com.sunft.chapter06;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 * 测试IntConsumer
 */
public class IntConsumerDemo {

    static int[] arr = {0, 1, 3, 4, 5, 6, 7, 8, 9, 10};

    private static void test01() {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    private static void test02() {
        Arrays.stream(arr).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                System.out.print(value + " ");
            }
        });
    }

    private static void test03() {
        Arrays.stream(arr).forEach((final int x) -> {
            System.out.print(x + " ");
        });
    }

    private static void test04() {
        Arrays.stream(arr).forEach((x) -> {
            System.out.print(x + " ");
        });
    }

    private static void test05() {
        Arrays.stream(arr).forEach((x) ->
            System.out.print(x + " ")
        );
    }

    public static void main(String[] args) {
        System.out.println("traditionalMethod:");
        test01();
        System.out.println();
        System.out.println("streamMethod:");
        test02();
        test05();
    }

}
