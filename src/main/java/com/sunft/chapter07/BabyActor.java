package com.sunft.chapter07;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

/**
 * 一个Baby的Actor示例
 */
public class BabyActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static enum Msg {
        SLEEP, PLAY, CLOSE
    }

    /**
     * 表示一种Actor的状态，更重要的是它封装了在这种状态下的消息处理逻辑。
     */
    Procedure<Object> angry = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            System.out.println("angryApply:" + param);
            if (param == Msg.SLEEP) {
                getSender().tell("I am already aangry.", getSelf());
                System.out.println("I am already aangry.");
            } else if (param == Msg.PLAY) {
                System.out.println("I like playing.");
                getContext().become(happy);
            }
        }
    };

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            System.out.println("happyApply:" + param);
            if (param == Msg.PLAY) {
                getSender().tell("I am already happy:-)", getSelf());
                System.out.println("I am already happy:-)");
            } else if (param == Msg.SLEEP) {
                System.out.println("I don't want to sleep.");
                getContext().become(angry);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("onReceive:" + message);
        if (message == Msg.SLEEP) {
            getContext().become(angry);
        } else if (message == Msg.PLAY) {
            getContext().become(happy);
        } else {
            unhandled(message);
        }

    }

}
