package com.sunft.chapter07;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * 一般来说，一个应用程序只需要一个ActorSystem就够用了。
 * ActorSystem.create()函数的第一个参数"Hello"为系统名称，
 * 第二个参数为配置文件。
 */
public class HelloMainSimple {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("samplehello.conf"));
        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
        System.out.println("HelloWorld Actor Path:" + a.path());
    }

}
