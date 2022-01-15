package com.datou.n4.section4;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

public class Section4_6 {
    public static void main(String[] args) {
        Chopstick4 c1 = new Chopstick4("1");
        Chopstick4 c2 = new Chopstick4("2");
        Chopstick4 c3 = new Chopstick4("3");
        Chopstick4 c4 = new Chopstick4("4");
        Chopstick4 c5 = new Chopstick4("5");

        new Philosopher4("a", c1, c2).start();
        new Philosopher4("b", c2, c3).start();
        new Philosopher4("c", c3, c4).start();
        new Philosopher4("d", c4, c5).start();
        new Philosopher4("e", c5, c1).start();
    }
}

@Slf4j(topic = "Chopstick")
class Chopstick4 extends ReentrantLock {
    String name;
    public Chopstick4(String name){
        this.name = name;
    }
}


@Slf4j(topic = "Philosopher")
class Philosopher4 extends Thread{
    Chopstick4 left;
    Chopstick4 right;

    public Philosopher4(String name, Chopstick4 left, Chopstick4 right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (left.tryLock()) {
                    if (right.tryLock()) {
                        try {
                            log.debug("eating...");
                            ThreadUtil.sleep(1);
                        } finally {
                            right.unlock();
                        }
                    }
                }
            } finally {
                left.unlock();
            }
        }
    }
}
