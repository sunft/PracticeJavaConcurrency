package com.sunft.chapter05;

import java.util.concurrent.Callable;

/**
 * 使用JDK自带的Future模式实现
 */
public class FutureReadData implements Callable<String> {

    private String para;

    public FutureReadData(String para) {
        this.para = para;
    }

    @Override
    public String call() throws Exception {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append(para);
            Thread.sleep(100);
        }
        return sb.toString();
    }

}
