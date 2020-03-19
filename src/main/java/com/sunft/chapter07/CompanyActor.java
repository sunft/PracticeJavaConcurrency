package com.sunft.chapter07;

import akka.actor.UntypedActor;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

/**
 * 代表公司账户的Actor
 */
public class CompanyActor extends UntypedActor {
    private Ref.View<Integer> count = STM.newRef(100);

    @Override
    public void onReceive(Object message) throws Throwable {
        // 首先判断接收的message是否是Coordinated。如果是Coordinated，
        // 则表示这是一个新事务的开始。
        if (message instanceof Coordinated) {
            final Coordinated c = (Coordinated) message;
            // 获得事务的参数，也就是需要转账的金额。
            final int downCount = (int) c.getMessage();
            // 将employee加入当前事务中，这样这个事务中就有两个参与者了。
            STMDemo.employee.tell(c.coordinate(downCount), getSelf());
            try {
                // 定义原子执行块作为这个事务的一部分。
                c.atomic(new Runnable() {
                    @Override
                    public void run() {
                        if (count.get() < downCount) {
                            throw new RuntimeException("less than " + downCount);
                        }
                        STM.increment(count, -downCount);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("GetCount".equals(message)) {
            getSender().tell(count.get(), getSelf());
        } else {
            unhandled(message);
        }
    }

}
