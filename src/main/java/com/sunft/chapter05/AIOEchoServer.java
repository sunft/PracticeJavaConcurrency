package com.sunft.chapter05;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * AIO EchoServer的实现
 */
public class AIOEchoServer {

    public final static int PORT = 8000;
    private AsynchronousServerSocketChannel server;

    public AIOEchoServer() throws IOException {
        server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));
    }

    public void start() {
        System.out.println("Sever listen on " + PORT);
        /**
         * 注册事件和事件完成后的处理器
         * accept方法会立即返回，它并不会真的等待客户端的到来
         * 它的第一个参数是一个附件，可以是任意类型，作用是让当前线程和后续的回调方法可以共享信息，
         * 它会在后续调用中传递给handler。
         *
         * accept方法实际上做了两件事：
         * 第一，发起accept请求，告诉系统可以开始监听端口了。
         * 第二，注册CompletionHandler实例，告诉系统一旦有客户端前来连接，如果连接成功，
         * 就去执行CompletionHandler.completed()方法；如果连接失败，就去执行CompletionHandler.failed()方法。
         * 所以，server.accept()方法不会阻塞，它会立即返回。
         */
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);

            /**
             *
             * @param result
             * @param attachment
             */
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                System.out.println(Thread.currentThread().getName());
                Future<Integer> writeResult = null;
                try {
                    buffer.clear();
                    //这里会立即返回，返回的结果是一个Future
                    //这里直接调用Future.get()方法进行等待，将这个异步方法变成了同步方法
                    result.read(buffer).get(100, TimeUnit.SECONDS);
                    buffer.flip();
                    //这里也是立即返回，并不会等数据完全写完
                    writeResult = result.write(buffer);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //进行下一个连接准备
                        server.accept(null, this);
                        //这里会等待，直到数据完全写完
                        writeResult.get();
                        //关闭当前正在处理的客户端
                        result.close();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("failed: " + exc);
            }

        });
    }

    public static void main(String[] args) throws Exception {
        new AIOEchoServer().start();
        // 主线程可以继续自己的行为
        while (true) {
            Thread.sleep(1000);
        }
    }

}
