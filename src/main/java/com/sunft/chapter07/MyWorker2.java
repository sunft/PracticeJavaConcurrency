package com.sunft.chapter07;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyWorker2 extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static enum Msg {
        WORKING, DONE, CLOSE
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            int i = (int) message;
            Thread.sleep(1000);
            getSender().tell(i * i, getSelf());
        }

        if (message == Msg.DONE) {
            log.info("Stop working.");
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
