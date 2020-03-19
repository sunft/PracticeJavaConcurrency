package com.sunft.chapter06;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法引用举例
 */
public class InstanceMethodRef {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User(i, "billy" + i));
        }
        users.stream().map(User::getName).forEach(System.out::println);
    }

}
