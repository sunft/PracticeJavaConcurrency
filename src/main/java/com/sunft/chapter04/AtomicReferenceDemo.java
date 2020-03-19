package com.sunft.chapter04;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference转账示例
 * ABA问题导致重复设置值
 */
public class AtomicReferenceDemo {

    /**
     * 设置账户初始值小于20，显然这是一个需要被充值的账户
     */
    static AtomicReference<Integer> money = new AtomicReference<>(19);

    public static void main(String[] args) {
        //模拟多个线程同时更新后台数据库，为用户充值
        for (int i = 0; i < 3; i++) {
            recharge();
        }

        //用户消费线程，模拟消费行为
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    while (true) {
                        Integer m = money.get();
                        if (m > 10) {
                            System.out.println("大于10元");
                            if (money.compareAndSet(m, m - 10)) {
                                System.out.println("成功消费10元，余额：" + money.get());
                                break;
                            }
                        } else {
                            System.out.println("没有足够的金额");
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private static void recharge() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    while (true) {
                        Integer m = money.get();
                        if (m < 20) {
                            if (money.compareAndSet(m, m + 20)) {
                                System.out.println("余额小于20元，充值成功，余额：" + money.get() + "元");
                                break;
                            } else {
                                System.out.println("余额大于20元，无须充值");
                                break;
                            }
                        }
                    }
                }
            }
        }.start();
    }

}
