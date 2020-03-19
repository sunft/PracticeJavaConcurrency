package com.sunft.chapter05;

/**
 * Client主要实现了获取FutureData，并开启构造RealData的线程。
 * 并在接收请求后，很快的返回FutureData。注意，它不会等待数据
 * 真的构造完毕再返回，而是立即返回FutureData，即使这个时候
 * FutureData内并没有真实数据。
 */
public class Client {

    public Data request(final String queryStr) {
        final FutureData future = new FutureData();
        new Thread() {
            @Override
            public void run() {
                //ReadData的构建很慢，所以在单独的线程中进行
                RealData realData = new RealData(queryStr);
                future.setRealData(realData);
            }
        }.start();
        return future;
    }

    public static void main(String[] args) {
        Client client = new Client();
        //这里会立即返回，因为得到的是FutureData而不是ReadData
        Data data = client.request("name");
        System.out.println("请求完毕");

        try {
            //这里可以用一个sleep代替了对其他业务逻辑的处理
            //在处理这些业务逻辑的过程中，RealData被创建，从而充分利用了等待时间
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //使用真实的数据
        System.out.println("数据 = " + data.getResult());
    }

}
