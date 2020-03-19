package com.sunft.chapter05;

/**
 * RealData是最终需要使用的数据模型。它的构造很慢。
 * 在这里，使用sleep()函数模拟这个过程，简单地模拟一个字符串的构造。
 */
public class RealData implements Data {

    protected final String result;

    public RealData(String para) {
        //RealData的构造可能很慢，需要用户等待很久，这里使用sleep模拟
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i ++) {
            sb.append(para);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        result = sb.toString();
    }

    @Override
    public String getResult() {
        return result;
    }

}
