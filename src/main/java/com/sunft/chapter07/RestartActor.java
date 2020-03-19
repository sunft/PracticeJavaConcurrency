package com.sunft.chapter07;

import akka.actor.UntypedActor;
import scala.Option;

/**
 * 待重启的demo
 */
public class RestartActor extends UntypedActor {

    public enum Msg {
        DONE, RESTART
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("preStart hashcode:" + this.hashCode());
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("postStop hashcode:" + this.hashCode());
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        System.out.println("preRestart hashCode:" + this.hashCode());
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        super.postRestart(reason);
        System.out.println("postRestart hashCode:" + this.hashCode());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.DONE) {
            getContext().stop(getSelf());
        } else if (message == Msg.RESTART) {
            System.out.println(((Object) null).toString());
            //抛出异常，默认会被restart，但这里会resume
            double a = 0 / 0;
        }
        unhandled(message);
    }

}