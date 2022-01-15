package com.datou.n4.section3;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 有五位哲学家，围坐在圆桌旁。
 *      他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。
 *      吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。
 *      如果筷子被身边的人拿着，自己就得等待
 *
 *
 * 执行不多会，就执行不下去了 --- > 死锁
 * 这种线程没有按预期结束，执行不下去的情况，归类为【活跃性】问题，除了死锁以外，还有活锁和饥饿者两种情况
 */
@Slf4j
public class Section3_2 {
    public static void main(String[] args) {
        Chopstick3 c1 = new Chopstick3("1");
        Chopstick3 c2 = new Chopstick3("2");
        Chopstick3 c3 = new Chopstick3("3");
        Chopstick3 c4 = new Chopstick3("4");
        Chopstick3 c5 = new Chopstick3("5");

        new Philosopher3("a", c1, c2).start();
        new Philosopher3("b", c2, c3).start();
        new Philosopher3("c", c3, c4).start();
        new Philosopher3("d", c4, c5).start();
        new Philosopher3("e", c5, c1).start();
    }
}

@Slf4j(topic = "Chopstick")
class Chopstick3 {        // 筷子类
    String name;
    public Chopstick3(String name) {
        this.name = name;
    }
}

@Slf4j(topic = "Philosopher")
class Philosopher3 extends Thread { //哲学家类
    Chopstick3 left;
    Chopstick3 right;
    public Philosopher3(String threadName, Chopstick3 left, Chopstick3 right) {
        super(threadName);
        this.left = left;
        this.right = right;
    }
    @Override
    public void run() {
        while (true) {
            // 获得左手筷子
            synchronized (left) {
                // 获得右手筷子
                synchronized (right) {
                    // 吃饭
                    log.debug("eating...");
                    ThreadUtil.sleep(1);
                }
                // 放下右手筷子
            }
            // 放下左手筷子
        }
    }
}
