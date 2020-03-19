package com.sunft.chapter07;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AgentDemo {

    public static Agent<Integer> counterAgent = Agent.create(0, ExecutionContexts.global());

    static ConcurrentLinkedQueue<Future<Integer>> futures = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws TimeoutException {
        final ActorSystem system = ActorSystem.create("agentdemo",
                ConfigFactory.load("samplehello.conf"));
        //创建10个CounterActor对象
        ActorRef[] counter = new ActorRef[10];
        for (int i = 0; i < counter.length; i++) {
            counter[i] = system.actorOf(Props.create(CounterActor.class), "counter_" + i);
        }

        //使用Inbox与CounterActor进行通信
        final Inbox inbox = Inbox.create(system);
        for (ActorRef actorRef : counter) {
            inbox.send(actorRef, 1);
            inbox.watch(actorRef);
        }

        int closeCount = 0;
        //等待所有Actor全部结束
        while (true) {
            Object msg = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if (msg instanceof Terminated) {
                closeCount++;
                if (closeCount == counter.length) {
                    break;
                }
            } else {
                System.out.println(msg);
            }
        }

        //等待所有的累加线程完成，因为它们都是异步的
        //将所有的Future进行串行组合(使用sequence()方法)
        Futures.sequence(futures, system.dispatcher()).onComplete(
                new OnComplete<Iterable<Integer>>() {
                    @Override
                    public void onComplete(Throwable failure, Iterable<Integer> success) throws Throwable {
                        System.out.println("counterAgent= " + counterAgent.get());
                        system.shutdown();
                    }
                }, system.dispatcher());

    }
}
