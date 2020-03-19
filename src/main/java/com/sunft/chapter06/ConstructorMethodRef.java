package com.sunft.chapter06;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造函数的方法引用
 */
public class ConstructorMethodRef {

    @FunctionalInterface
    interface UserFactory<U extends User> {
        U create(int id, String name);
    }

    static UserFactory<User> uf = User::new;

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            users.add(uf.create(i, "billy" + Integer.toString(i)));
        }
        users.stream().map(User::getName).forEach(System.out::println);
    }

}
