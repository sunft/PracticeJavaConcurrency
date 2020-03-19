package com.sunft.chapter07;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Printer extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static enum Msg {
        WORKING, DONE, CLOSE
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            System.out.println("Printer:" + message);
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
