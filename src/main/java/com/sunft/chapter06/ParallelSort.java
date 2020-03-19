package com.sunft.chapter06;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 并行排序：在Java 8中可以使用新增的Arrays.parallelSort()方法直接使用并行排序。
 */
public class ParallelSort {

    public static void main(String[] args) {
        int[] arr = new int[10000000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ThreadLocalRandom.current().nextInt(4, 77);
        }
        Arrays.parallelSort(arr);
        int count = 0;
        for (int value : arr) {
            count++;
            if (count % 10 == 0) {
                System.out.println();
            }
            System.out.print(value + " ");
        }
    }

}
