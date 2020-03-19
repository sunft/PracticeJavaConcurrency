package com.sunft.chapter07;

import akka.actor.UntypedActor;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

public class EmployeeActor extends UntypedActor {

    /**
     * 雇员初始金额是50元。
     */
    private Ref.View<Integer> count = STM.newRef(50);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Coordinated) {
            final Coordinated c = (Coordinated) message;
            final int downCount = (int) c.getMessage();
            try {
                c.atomic(new Runnable() {
                    @Override
                    public void run() {
                        STM.increment(count, downCount);
                    }
                });
            } catch (Exception e) {

            }
        } else if ("GetCount".equals(message)) {
            getSender().tell(count.get(), getSelf());
        } else {
            unhandled(message);
        }
    }

}
