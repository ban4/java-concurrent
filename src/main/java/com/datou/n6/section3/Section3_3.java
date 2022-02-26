package com.datou.n6.section3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import static com.datou.util.ThreadUtil.sleep;

/**
 * ABA 解决
 * AtomicStampedReference
 *  可以给原子引用加上版本号，从而解决ABA问题，追踪原子引用整个的变化过程，引用变量中途被更改了几次（如: A -> B -> A -> C ）
 */
@Slf4j
public class Section3_3 {
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);

    public static void main(String[] args) {
        log.debug("main start...");
        // 获取值 A
        String prev = ref.getReference();
        // 获取版本号
        int stamp = ref.getStamp();
        other();
        sleep(2);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));
    }

    private static void other() {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", ref.getStamp(), ref.getStamp() + 1));
        }, "t1").start();
        sleep(1);
        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", ref.getStamp(), ref.getStamp() + 1));
        }, "t2").start();
    }
}


