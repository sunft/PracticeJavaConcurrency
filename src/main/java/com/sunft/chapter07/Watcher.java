package com.sunft.chapter07;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息路由的使用方式
 */
public class Watcher extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     * 路由器组件
     */
    public Router router;

    /**
     * 指定路由策略和一组被路由的Actor(Routee)
     */ {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorRef worker = getContext().actorOf(Props.create(MyWorker.class), "worker" + i);
            getContext().watch(worker);
            routees.add(new ActorRefRoutee(worker));
        }
        //这里使用RoundRobinRoutingLogic路由策略，也就是对所有的Routee进行轮询消息发送
        //在本例中，routee由5个MyWorker Actor构成
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof MyWorker.Msg) {
            //将消息投递给Router
            router.route(message, getSender());
        } else if (message instanceof Terminated) {
            //当一个MyWorker停止工作时，简单地将其从工作组中移除
            router = router.removeRoutee(((Terminated) message).actor());
            System.out.println(((Terminated) message).actor().path() + " is closed, routees = " + router.routees().size());
            //如果发现系统中没有可用的Actor，则会直接关闭系统
            if (router.routees().size() == 0) {
                System.out.println("Close system.");
                RouteMain.flag.send(false);
                getContext().system().shutdown();
            }
        } else {
            unhandled(message);
        }
    }

}
