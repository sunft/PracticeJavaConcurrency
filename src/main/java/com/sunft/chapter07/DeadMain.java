package com.sunft.chapter07;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class DeadMain {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem
                .create("deadwatch", ConfigFactory.load("samplehello.conf"));
        ActorRef worker = system.actorOf(Props.create(MyWorker.class), "worker");
        system.actorOf(Props.create(WatchActor.class, worker), "watcher");
        worker.tell(MyWorker.Msg.WORKING, ActorRef.noSender());
        worker.tell(MyWorker.Msg.DONE, ActorRef.noSender());
        //PoisonPill意为毒药丸，它会直接“毒死”接收方，让其终止。
        worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }

}
