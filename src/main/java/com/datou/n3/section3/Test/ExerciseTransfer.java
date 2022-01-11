package com.datou.n3.section3.Test;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 转账练习
 *  下面代码是否存在线程安全问题，并尝试改正
 */
@Slf4j(topic = "log")
public class ExerciseTransfer {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, randomAmount());
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, randomAmount());
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // 查看转账2000次后的总金额
        log.debug("total:{}", (a.getMoney() + b.getMoney()));

    }

    static Random random = new Random();  // Random 为线程安全 随机 1~100
    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }

}

// 账户
class Account {
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * 存在线程安全问题，应该加锁
     */
    public void transfer(Account target, int amount) {
        if (this.money > amount) {
            this.setMoney(this.getMoney() - amount);
            target.setMoney(target.getMoney() + amount);
        }
    }

    /**
     * 存在线程安全问题，
     *  synchronized 方法，锁当前对象，a、b为不同对象
     */
//    public synchronized void transfer(Account target, int amount) {
//        if (this.money > amount) {
//            this.setMoney(this.getMoney() - amount);
//            target.setMoney(target.getMoney() + amount);
//        }
//    }

    /**
     * 不存在线程安全问题，
     *  synchronized 锁同一对象，
     *  等效于 public synchronized static void transfer(Account target, int amount) {
     */
//    public void transfer(Account target, int amount) {
//        synchronized(Account.class){
//            if (this.money > amount) {
//                this.setMoney(this.getMoney() - amount);
//                target.setMoney(target.getMoney() + amount);
//            }
//        }
//    }
}