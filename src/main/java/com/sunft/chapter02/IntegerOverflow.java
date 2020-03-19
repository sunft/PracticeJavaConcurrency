package com.sunft.chapter02;

/**
 * 整型溢出
 */
public class IntegerOverflow {

    public static void main(String[] args) {
        int v1 = 1073741827;
        int v2 = 1431655768;

        System.out.println("v1 = " + v1);
        System.out.println("v2 = " + v2);
        int ave = (v1 + v2) / 2;
        System.out.println("ave = " + ave);
    }

}
