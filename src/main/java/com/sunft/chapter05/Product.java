package com.sunft.chapter05;

/**
 * 不变模式举例
 */
public final class Product {//确保无子类

    /**
     * 私有属性，不会被其他对象获取
     * final保证属性不会被2次赋值
     */
    private final String no;
    private final String name;
    private final double price;

    /**
     * 在构造时必须制定数据，因为创建之后，无法进行修改
     *
     * @param no
     * @param name
     * @param price
     */
    public Product(String no, String name, double price) {
        super();
        this.no = no;
        this.name = name;
        this.price = price;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
