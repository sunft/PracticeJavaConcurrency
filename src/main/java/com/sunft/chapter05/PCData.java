package com.sunft.chapter05;

/**
 * 作为生产者和消费者之间的共享数据模型
 */
public final class PCData {

    private final int intData;

    public PCData(int d) {
        this.intData = d;
    }

    public PCData(String d) {
        intData = Integer.valueOf(d);
    }

    public int getData() {
        return intData;
    }

    @Override
    public String toString() {
        return "data:" + intData;
    }
}
