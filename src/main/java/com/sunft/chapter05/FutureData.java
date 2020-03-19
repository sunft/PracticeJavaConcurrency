package com.sunft.chapter05;

/**
 * 真实数据RealData的代理，封装了获取RealData的等待过程。
 */
public class FutureData implements Data {

    /**
     * FutureData是RealData的包装
     */
    protected RealData realData = null;

    protected boolean isReady = false;

    public synchronized void setRealData(RealData realData) {
        if (isReady) {
            return;
        }
        this.realData = realData;
        isReady = true;
        //RealData已经被注入，通知getResult()
        notifyAll();
    }

    @Override
    public synchronized String getResult() {
        while (!isReady) {
            try {
                //一直等待，直到RealData被注入
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.result;
    }

}
