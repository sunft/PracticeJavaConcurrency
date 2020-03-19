package com.sunft.chapter01;

/**
 * 指令重排序举例
 * 如果writer发生了指令重排序，reader方法在判断flag为true时，
 * a可能还没被赋值为1。
 */
public class OrderExample {
    int a = 0;
    boolean flag = false;

    public void writer() {
        a = 1;
        flag = true;
    }

    public void reader() {
        if (flag) {
            int i = a + 1;
        }

        Thread thread;
    }
}
