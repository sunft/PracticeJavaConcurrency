package com.sunft.chapter07;

import akka.actor.UntypedActor;

/**
 * Akka之Hello World
 * 当Greeter收到GREET消息时，就会在控制台打印“Hello World”，
 * 并且向消息发送发发送DONE消息。
 */
public class Greeter extends UntypedActor {

    public static enum Msg {
        GREET, DONE;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.GREET) {
            System.out.println("Hello World!");
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(message);
        }
    }
}
