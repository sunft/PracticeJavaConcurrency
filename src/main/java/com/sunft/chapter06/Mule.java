package com.sunft.chapter06;

/**
 * 骡子类
 */
public class Mule implements IHourse, IDonkey, IAnimal {

    /**
     * 重新实现一下run()方法，让编译器可以进行方法绑定
     */
    @Override
    public void run() {
        System.out.println("Mule run");
    }

    @Override
    public void eat() {
        System.out.println("Mule eat");
    }

    public static void main(String[] args) {
        Mule m = new Mule();
        m.run();
        m.breath();
    }

}
