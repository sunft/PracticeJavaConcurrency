package com.sunft.chapter06;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 * 测试AndThen
 */
public class AndThenDemo {

    static int[] arr = {1, 3, 4, 5, 6, 7, 8, 9, 10};

    public static void main(String[] args) {
        IntConsumer outPrintln = System.out::println;
        IntConsumer errPrintln = System.err::println;
        Arrays.stream(arr).forEach(outPrintln.andThen(errPrintln));
    }

}
