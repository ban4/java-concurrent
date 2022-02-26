package com.datou.n6.section5;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicReferenceFieldUpdater // 域 字段
 * AtomicIntegerFieldUpdater
 * AtomicLongFieldUpdater
 */
public class Section5_1 {
    private volatile int field;

    public static void main(String[] args) {
        AtomicIntegerFieldUpdater fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Section5_1.class, "field");

        Section5_1 test5 = new Section5_1();
        fieldUpdater.compareAndSet(test5, 0, 10);

        // 修改成功 field = 10
        System.out.println(test5.field);

        // 修改成功 field = 20
        fieldUpdater.compareAndSet(test5, 10, 20);
        System.out.println(test5.field);

        // 修改失败 field = 20
        fieldUpdater.compareAndSet(test5, 10, 30);
        System.out.println(test5.field);
    }
}
