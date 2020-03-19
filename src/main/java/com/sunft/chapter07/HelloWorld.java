package com.sunft.chapter07;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * 与Greeter交流的另外一个Actor是HelloWorld
 */
public class HelloWorld extends UntypedActor {
    ActorRef greeter;

    /**
     * 该方法会被Akka框架调用，完成一些初始化的工作。
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter Actor Path:" + greeter.path());
        greeter.tell(Greeter.Msg.GREET, getSelf());
    }

    /**
     * HelloWorld的消息处理函数。在这里只处理DONE的消息。
     * 在收到DONE消息后，它会再向Greeter发送GREET消息，接着将停止自己
     * @param message
     * @throws Throwable
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Greeter.Msg.DONE) {
            greeter.tell(Greeter.Msg.GREET, getSelf());
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }
}
