package com.sunft.chapter07;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * 一个带有生命周期回调函数的Actor
 */
public class MyWorker extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static enum Msg {
        WORKING, DONE, CLOSE
    }

    /**
     * 一般用于初始化资源
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        System.out.println("MyWorker is starting");
    }

    /**
     * 一般用于资源的释放
     * @throws Exception
     */
    @Override
    public void postStop() throws Exception {
        System.out.println("MyWorker is stopping.");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.WORKING) {
            System.out.println("I am working.");
        }

        if (message == Msg.DONE) {
            System.out.println("Stop working.");
        }

        if (message == Msg.CLOSE) {
            log.info("I will shutdown.");
            getSender().tell(Msg.CLOSE, getSelf());
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }
}
