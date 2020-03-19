package com.sunft.chapter07;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * 监视者：监视者如同一个劳动监工，一旦MyWorker因为意外停止工作，
 * 监视者就会收到一个通知。
 */
public class WatchActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public WatchActor(ActorRef ref) {
        getContext().watch(ref);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Terminated) {
            System.out.println(String.format("%s has terminated, shutting down system", ((Terminated)message).getActor().path()));
            getContext().system().shutdown();
        } else {
            unhandled(message);
        }
    }

}
