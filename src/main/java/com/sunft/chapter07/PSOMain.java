package com.sunft.chapter07;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * 粒子群算法最优解
 */
public class PSOMain {

    /**
     * 定义10万个粒子
     */
    public static final int BIRD_COUNT = 100000;

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("psoSystem", ConfigFactory.load("samplehello.conf"));
        /**
         * 创建一个MasterBird Actor
         */
        system.actorOf(Props.create(MasterBird.class), "masterbird");
        for (int i = 0; i < BIRD_COUNT; i++) {
            system.actorOf(Props.create(Bird.class), "bird_" + i);
        }
    }

}
