package com.sunft.chapter07;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.transactor.Coordinated;
import akka.util.Timeout;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

/**
 * 场景模拟：
 * 假设有一个公司要给员工发放福利，公司账户里有100元。每次公司账户会给员工
 * 账户转一笔钱，假设转账10元，那么公司账户中应该减去10元，同时，员工账户
 * 中应该增加10元。这两个操作必须同时完成，或者同时不完成。
 * 看下主函数中是如何启动一个内存事务的。
 */
public class STMDemo {

    public static ActorRef company = null;
    public static ActorRef employee = null;

    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("transactionDemo",
                ConfigFactory.load("samplehello.conf"));
        company = system.actorOf(Props.create(CompanyActor.class), "company");
        employee = system.actorOf(Props.create(EmployeeActor.class), "employee");

        Timeout timeout = new Timeout(1, TimeUnit.SECONDS);

        // 进行了19次汇款，第一次汇款额度为1元，第二次为2元，以此类推，
        // 最后一笔汇款为19元
        for (int i = 1; i < 20; i++) {
            // 建立一个Coordinated协调者，并且将这个协调者当作消息发送给company
            // 当company收到这个协调者消息后，自动成为这个事务的第一个成员。
            company.tell(new Coordinated(i, timeout), ActorRef.noSender());
            Thread.sleep(200);
            Integer companyCount = (Integer) Await.result(
                    ask(company, "GetCount", timeout), timeout.duration()
            );
            Integer employeeCount = (Integer) Await.result(
                    ask(employee, "GetCount", timeout), timeout.duration()
            );
            System.out.println("company count = " + companyCount);
            System.out.println("employee count = " + employeeCount);
            System.out.println("==================================");
        }

    }

}
