package com.sunft.chapter06;

import java.util.Arrays;

/**
 * 简化代码示例
 */
public class SimplyCode {

    public static void simplyCode() {
        int[] arr = {1, 3, 4, 5, 6, 7, 8, 9, 10};

        //使用传统方式
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 != 0) {
                arr[i]++;
            }
            System.out.print(arr[i]);
        }
        System.out.println();

        //使用函数式编程
        Arrays.stream(arr).map(x -> (x % 2 == 0 ? x : x + 1)).forEach(System.out::print);
        System.out.println();
    }

    public static void main(String[] args) {
        simplyCode();
    }

}
