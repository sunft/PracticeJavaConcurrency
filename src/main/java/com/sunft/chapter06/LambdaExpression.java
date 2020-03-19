package com.sunft.chapter06;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * lambda表达式
 */
public class LambdaExpression {

    public static void test01() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        numbers.forEach((Integer value) -> System.out.print(value + " "));
    }

    public static void test02() {
        //这里的num如果不加final，值也无法修改
        final int num = 2;
        Function<Integer, Integer> stringConverter = (from) -> from * num;
        System.out.println(stringConverter.apply(3));
    }

    public static void main(String[] args) {
        test01();
        //test02();
    }

}
