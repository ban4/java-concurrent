package com.datou.n6.section3;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

import static com.datou.util.ThreadUtil.sleep;

/**
 * ABA 问题
 */
@Slf4j
public class Section3_2 {
    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) {
        log.debug("main start...");
        // 获取值 A
        String prev = ref.get();
        other();
        sleep(2);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C"));
    }

    private static void other() {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.get(), "B"));
        }, "t1").start();
        sleep(1);
        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.get(), "A"));
        }, "t2").start();
    }
}


