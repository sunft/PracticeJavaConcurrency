package com.sunft.chapter07;

import akka.actor.UntypedActor;
import akka.dispatch.Mapper;
import scala.concurrent.Future;


/**
 * 模拟场景：有10个Actor，它们一起对一个Agent执行累加操作，
 * 每个Agent累加10 000次，如果没有意外，那么Agent最终的值将
 * 是100 000；如果Actor间的调度出现问题，那么这个值可能小于
 * 100 000。
 * <p>
 * 在CounterActor的消息处理函数onReceive()中，对全局的counterAgent
 * 进行累加操作，alter()方法指定了累加动作的addMapper。由于我们希
 * 望在将来知道累加行为是否完成，因此在这里将返回的Future对象进行
 * 收集。完成任务后，Actor会自行退出。
 */
public class CounterActor extends UntypedActor {

    Mapper<Integer, Integer> addMapper = new Mapper<Integer, Integer>() {
        @Override
        public Integer apply(Integer parameter) {
            return parameter + 1;
        }
    };

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            for (int i = 0; i < 10000; i++) {
                //我希望能够知道Future何时结束
                Future<Integer> f = AgentDemo.counterAgent.alter(addMapper);
                AgentDemo.futures.add(f);
            }
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }

}
