package com.sunft.chapter05;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NIO服务端
 */
public class MultiThreadNIOEchoServer {

    /**
     * 统计服务器线程在一个客户端上花费的时间
     */
    public static Map<Socket, Long> time_stat = new HashMap<Socket, Long>(10240);

    /**
     * 封装了一个队列，保存在需要回复给这个客户端的所有信息上，
     * 这样再进行回复时，只要从outq对象中弹出元素即可。
     */
    static class EchoClient {
        private LinkedList<ByteBuffer> outq;

        EchoClient() {
            outq = new LinkedList<ByteBuffer>();
        }

        // Return the output queue.
        public LinkedList<ByteBuffer> getOutputQueue() {
            return outq;
        }

        // Enqueue a ByteBuffer on the output queue.
        public void enqueue(ByteBuffer bb) {
            outq.addFirst(bb);
        }
    }

    class HandleMsg implements Runnable {
        SelectionKey sk;
        ByteBuffer bb;

        public HandleMsg(SelectionKey sk, ByteBuffer bb) {
            this.sk = sk;
            this.bb = bb;
        }

        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) sk.attachment();
            // 数据入队，模拟处理数据
            echoClient.enqueue(bb);

            // We've enqueued data to be written to the client, we must
            // not set interest in OP_WRITE.
            //
            sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            //强迫selector立即返回
            selector.wakeup();
        }
    }

    /**
     * 用于处理所有的网络连接
     */
    private Selector selector;

    /**
     * 用于对每一个客户端进行相应的处理，每一个请求都会委托给线程池中的线程进行实例处理
     */
    private ExecutorService tp = Executors.newCachedThreadPool();

    /**
     * Accept a new client and set it up for reading.
     */
    private void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        //代表和客户端通信的通道
        SocketChannel clientChannel;
        try {
            clientChannel = server.accept();
            //将Channel配置为非阻塞模式，也就是要求系统在准备好IO后，再通知线程来读取或者写入
            clientChannel.configureBlocking(false);

            // Register this channel for reading.
            // 将新生成的Channel注册到选择器上，并告诉Selector现在对读(OP_READ)操作感兴趣。
            // 当Selector发现这个Channel已经准备好读时，就能给线程一个通知。
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            // Allocate an EchoClient instance and attach it to this selection key.
            // 创建一个实例代表一个客户端。
            EchoClient echoClient = new EchoClient();
            // 将这个客户端实例作为附件，附加到表示这个连接的SelectionKey上。
            // 这样在整个连接的处理过程中，我们都可以共享这个EchoClient实例。
            clientKey.attach(echoClient);

            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress() + ".");
        } catch (Exception e) {
            System.out.println("Failed to accept new client.");
            e.printStackTrace();
        }
    }

    /**
     * Read from a client. Enqueue the data on the clients output
     * queue and set the selector to notify on OP_WRITE.
     */
    private void doRead(SelectionKey sk) {
        // 得到当前客户端Channel
        SocketChannel channel = (SocketChannel) sk.channel();
        // 准备8K的缓冲区读取数据，所有读取的数据放在变量bb中
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;

        try {
            len = channel.read(bb);
            if (len < 0) {
                disconnect(sk);
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed to read from client.");
            e.printStackTrace();
            disconnect(sk);
            return;
        }

        // Flip the buffer.
        // 重置缓冲区，为数据处理做准备
        bb.flip();
        // 使用线程池进行数据处理，在单独的线程中处理，而不用阻塞任务派发线程
        tp.execute(new HandleMsg(sk, bb));
    }

    /**
     * Called when a SelectionKey is ready for writing.
     * @param sk 对于一个客户端来说，这个SelectionKey参数和函数doRead()拿到
     *           的SelectionKey参数是同一个。因此，通过SelectionKey参数就可
     *           以在这两个操作中共享EchoClient实例了。
     */
    private void doWrite(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        // 取得了EchoClient实例及它的发送内容列表
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();

        //获得顶部元素，准备写回客户端
        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(sk);
                return;
            }

            if (bb.remaining() == 0) {
                // The buffer was completely written, remove it.
                // 如果全部发送完成，则移除这个缓存对象
                outq.removeLast();
            }
        } catch (Exception e) {
            System.out.println("Failed to write to client.");
            e.printStackTrace();
            disconnect(sk);
        }

        // If there is no more data to be written, remove interest in
        // OP_WRITE.
        if (outq.size() == 0) {
            // 将写时间从感兴趣的操作中移除，如果不这么做，每次Channel准备好写时，都会来执行函数doWrite()
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void disconnect(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();

        InetAddress clientAddress = channel.socket().getInetAddress();
        System.out.println(clientAddress.getHostAddress() + " disconnected.");

        try {
            channel.close();
        } catch (Exception e) {
            System.out.println("Failed to close client socket channel.");
            e.printStackTrace();
        }
    }

    private void startServer() throws Exception {
        // 通过工厂方法获得一个Selector对象的实例
        selector = SelectorProvider.provider().openSelector();

        // Create non-blocking server socket.
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //将这个SocketChannel设置为非阻塞模式
        ssc.configureBlocking(false);

        // Bind the server socket to localhost.
//        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), 8000);
        InetSocketAddress isa = new InetSocketAddress(8000);
        ssc.socket().bind(isa);

        // Register the socket for select events.
        // SelectionKey表示一对Selector和Channel的关系，当Channel注册到Selector上时，
        // 就相当于确立了两者的服务关系，而SelectionKey就是这个契约。当Channel注册到
        // 或者Channel被关闭时，它们对应的SelectionKey就会失效。
        SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

        // Loop forever. 等待-分发网络消息。
        for (; ; ) {
            //该方法是一个阻塞方法。如果当前没有任何数据准备好，它就会等待。
            //一旦有数据可读，它就会返回。
            selector.select();
            if(selector.selectNow()==0){
                continue;
            }
            //获取那些准备好的SelectionKey。因为Selector同时为多个Channel服务，所以已经
            //准备就绪的Channel就有可能是多个，所以这里得到是一个集合
            Set<SelectionKey> readyKeys = selector.selectedKeys();

            //遍历这个集合，挨个处理所有的Channel数据
            Iterator<SelectionKey> i = readyKeys.iterator();
            long e = 0;
            while (i.hasNext()) {
                SelectionKey sk = i.next();
                // 将这个元素移除。注意，这个非常重要，当你处理完一个SelectionKey后，
                //务必将其从集合内删除，否则就会重复处理相同的SelectionKey
                i.remove();

                //判断当前SelectionKey所代表的Channel是否在Acceptable状态，如果是，
                //就进行客户端的接收(执行doAccept()方法)
                if (sk.isAcceptable()) {
                    doAccept(sk);
                    //判断Channel是否已经可以读了，如果是就进行读取
                } else if (sk.isValid() && sk.isReadable()) {
                    if (!time_stat.containsKey(((SocketChannel) sk.channel()).socket()))
                        time_stat.put(((SocketChannel) sk.channel()).socket(),
                                System.currentTimeMillis());
                    doRead(sk);
                    //判断通道是否准备好进行写
                } else if (sk.isValid() && sk.isWritable()) {
                    doWrite(sk);
                    e = System.currentTimeMillis();
                    long b = time_stat.remove(((SocketChannel) sk.channel()).socket());
                    System.out.println("spend:" + (e - b) + "ms");
                }
            }
        }
    }

    // Main entry point.
    public static void main(String[] args) {
        MultiThreadNIOEchoServer echoServer = new MultiThreadNIOEchoServer();
        try {
            echoServer.startServer();
        } catch (Exception e) {
            System.out.println("Exception caught, program exiting...");
            e.printStackTrace();
        }
    }
}
