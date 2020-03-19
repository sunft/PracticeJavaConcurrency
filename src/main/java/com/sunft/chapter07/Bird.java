package com.sunft.chapter07;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 用于表示基本粒子
 */
public class Bird extends UntypedActor {
    /**
     * 日志对象
     */
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     * 个体最优解
     */
    private PsoValue pBest = null;

    /**
     * 全局最优解
     */
    private PsoValue gBest = null;

    /**
     * 表示粒子在各个维度上的速度(在当前案例中，
     * 每一年的投资额可以认为是一个维度，因此系统有4个维度)
     */
    private List<Double> velocity = new ArrayList<>(5);

    /**
     * x表示投资方案，即每一年的投资额。
     */
    private List<Double> x = new ArrayList<>(5);

    /**
     * 由于在粒子群算法中，需要使用随机数，因此，这里定义了r。
     */
    private Random r = new Random();

    /**
     * 当一个粒子被创建时，我们需要初始化粒子的当前位置。
     * 粒子的每一个位置都代表了一个投资方案，下面的代码
     * 展示了粒子的初始化逻辑
     *
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        for (int i = 0; i < 5; i++) {
            velocity.add(Double.NEGATIVE_INFINITY);
            x.add(Double.NEGATIVE_INFINITY);
        }

        //x1 <= 400
        x.set(1, (double) r.nextInt(401));

        //x2 <= 440-1.1*x1；440的得来就是第一年全部存银行，第二年全部拿出来投资
        //第二年最多可以投资max这么多，使用随机数生成投资的额度
        double max = 440 - 1.1 * x.get(1);
        if (max < 0) {
            max = 0;
        }
        x.set(2, r.nextDouble() * max);

        //x3 <= 484-1.21*x1-1.1*x2
        max = 484 - 1.21 * x.get(1) - 1.1 * x.get(2);
        if (max <= 0) {
            max = 0;
        }
        x.set(3, r.nextDouble() * max);

        //x4 <= 532.4-1.331*x1-1.21*x2-1.1*x3
        max = 532.4 - 1.331 * x.get(1) - 1.21 * x.get(2) - 1.1 * x.get(3);
        if (max <= 0) {
            max = 0;
        }
        x.set(4, r.nextDouble() * max);

        //计算收益率
        double newFit = Fitness.fitness(x);
        //获取个体最优解
        pBest = new PsoValue(newFit, x);
        PBestMsg pBestMsg = new PBestMsg(pBest);
        ActorSelection selection = getContext().actorSelection("/user/masterbird");
        // 发送给Master
        selection.tell(pBestMsg, getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof GBestMsg) {
            //接收全局最优解
            gBest = ((GBestMsg) message).getValue();
            //根据粒子群的标准公式更新速度
            for (int i = 1; i < velocity.size(); i++) {
                updateVelocity(i);
            }
            //根据速度更新位置
            for (int i = 1; i < x.size(); i++) {
                updateX(i);
            }
            // 有效性检查
            validateX();
            // 计算新位置的适应度
            double newFit = Fitness.fitness(x);
            // 如果产生了新的个体最优解，就将其发送给Master
            if (newFit > pBest.getValue()) {
                pBest = new PsoValue(newFit, x);
                PBestMsg pBestMsg = new PBestMsg(pBest);
                getSender().tell(pBestMsg, getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    /**
     * 当前问题是有约束的，也就是说解空间并不是随意的。
     * 例子很可能在更新后，跑出了合理的范围，因此，还有
     * 必要进行有效性检查。
     * 对x1、x2、x3、x4进行约束，一旦发现粒子群跑出了
     * 定义范围就将它进行随机化。
     */
    private void validateX() {
        // x1
        if (x.get(1) > 400) {
            x.set(1, (double) r.nextInt(401));
        }

        // x2
        double max = 400 - 1.1 * x.get(1);
        if (x.get(2) > max || x.get(2) < 0) {
            x.set(2, r.nextDouble() * max);
        }

        // x3
        max = 484 - 1.21 * x.get(1) - 1.1 * x.get(2);
        if (x.get(3) > max || x.get(3) < 0) {
            x.set(3, r.nextDouble() * max);
        }

        // x4
        max = 532.4 - 1.331 * x.get(1) - 1.21 * x.get(2) - 1.1 * x.get(3);
        if (x.get(4) > max || x.get(4) < 0) {
            x.set(4, r.nextDouble() * max);
        }

    }

    /**
     * 在当前案例中，位置的更新是依据标准的粒子群实现的，
     * 位置的更新依赖于当前的速度。
     *
     * @param i
     * @return
     */
    private double updateX(int i) {
        double newX = x.get(i) + velocity.get(i);
        x.set(i, newX);
        return newX;
    }

    /**
     * 在当前案例中，速度的更新是依据标准的粒子群实现的
     *
     * @param i
     */
    private double updateVelocity(int i) {
        double v = Math.random() * velocity.get(i)
                + 2 * Math.random() * (pBest.getX().get(i) - x.get(i))
                + 2 * Math.random() * (gBest.getX().get(i) - x.get(i));
        v = v > 0 ? Math.min(v, 5) : Math.max(v, -5);
        velocity.set(i, v);
        return v;
    }

}
