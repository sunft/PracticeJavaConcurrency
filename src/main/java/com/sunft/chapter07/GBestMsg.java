package com.sunft.chapter07;

/**
 * 表示全局最优解
 */
public class GBestMsg {

    private final PsoValue value;

    public GBestMsg(PsoValue v) {
        value = v;
    }

    public PsoValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
