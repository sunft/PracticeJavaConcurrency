package com.sunft.chapter06;

import java.util.Arrays;

/**
 * 在函数式编程中，几乎所有传递的对象都不会被轻易修改。
 */
public class ImmutableObject {

    public static void changeArray() {
        int[] arr = {1, 3, 4, 5, 6, 7, 8, 9, 10};
        Arrays.stream(arr).map((x) -> x = x + 1).forEach(System.out::print);
        System.out.println();
        //这里打印出来的数组还是原来的值，并没有真正去修改
        Arrays.stream(arr).forEach(System.out::print);
    }

    public static void main(String[] args) {
        changeArray();
    }

}
