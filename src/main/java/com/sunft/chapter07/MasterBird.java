package com.sunft.chapter07;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * 用于管理和通知全局最优解
 */
public class MasterBird extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private PsoValue gBest = null;

    @Override
    public void onReceive(Object message) throws Throwable {
        // 当它收到一个个体最优解时，会将其与全局最优解进行比较，
        // 如果产生了新的全局最优解，就更新这个全局最优解并通知所有的粒子
        if (message instanceof PBestMsg) {
            PsoValue pBest = ((PBestMsg) message).getValue();
            if (gBest == null || gBest.getValue() < pBest.getValue()) {
                // 更新全局最优解，通知所有粒子
                System.out.println(message + "\n");
                gBest = pBest;
                ActorSelection selection = getContext().actorSelection("/user/bird_*");
                selection.tell(new GBestMsg(gBest), getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
