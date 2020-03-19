package com.sunft.chapter06;

import java.util.Arrays;

/**
 * 使用普通方法和函数式编程遍历数组
 */
public class IterateArray {

    /**
     * 使用普通的方式循环
     */
    public static void imperative() {
        int[] iArr = {1, 3, 4, 5, 6, 9, 8, 7, 4, 2};
        for (int i = 0; i < iArr.length; i++) {
            System.out.println(iArr[i]);
        }
    }

    /**
     * 使用函数式编程
     */
    public static void declarative() {
        int[] iArr = {1, 3, 4, 5, 6, 9, 8, 7, 4, 2};
        Arrays.stream(iArr).forEach(System.out::println);
    }

}
