package com.datou.n6.section1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.datou.util.ThreadUtil.join;

public class Section1_1 {

    public static void main(String[] args) {
        Account.demoTest(new AccountUnsafe(10000));

        Account.demoTest(new AccountSafe(10000));
    }

}

/**
 * 线程安全
 *   其中的关键是 compareAndSet，它的简称就是 CAS (也有 Compare And Swap 的说法)，它必须是原子操作。
 *
 *   compareAndSet（先比较，在set）正是做这个检查 在set前，先比较 prev 与 当前值 是否一致，
 *        一致：     以 calc 设置为新值，返回 true 表示成功
 *        不一致：    calc 作废，返回 false 表示失败
 *
 *   eg:
 *    当前金额 prev=1000，计算后金额 calc=prev-10=990，
 *    别的线程已经做了其他操作，当前值改成了2000
 *    那么本线程的这次 990 compareAndSet(prev,calc) 就作废了（prev != 2000）
 *
 *    进入 while 下次循环重试，
 *
 *    获取当前金额 prev = 2000，计算后金额 calc=prev-10=1990
 *    那么本线程的这次 1990 compareAndSet(prev,calc)返回true表示成功，calc设置为新值
 *
 * 注意：
 *  1、其实 CAS 的底层是 lock cmpxchg 指令(X86 架构)，在单核 CPU 和多核 CPU 下都能够保证【比较-交 换】的原子性。
 *  2、在多核状态下，某个核执行到带 lock 的指令时，CPU 会让总线锁住，当这个核把此指令执行完毕，再开启总线。
 * 这个过程中不会被线程的调度机制所打断，保证了多个线程对内存操作的准确性，是原子 的。
 */
class AccountSafe implements Account{
    private AtomicInteger balance;
    public AccountSafe(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }
    @Override
    public Integer getBalance() {
        return balance.get();
    }
    @Override
    public void withdraw(Integer amount) {
        // 需要不断尝试，直到成功为止
        while (true){
            int prev = balance.get();   // 当前线程 获取到的金额
            int calc = prev - amount;   // 计算后的金额

            if (balance.compareAndSet(prev, calc)){
                break;
            }
        }
        // 可以简化为下面的方法
        // balance.addAndGet(-1 * amount);
    }
}

/**
 * 线程不安全
 *
 * 线程安全需要给 withdraw getbalance 加锁 synchronized
 */
class AccountUnsafe implements Account{
    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        return balance;
    }

    @Override
    public void withdraw(Integer amount) {
        balance -= amount;
    }
}

interface Account{
    // 获取余额
    Integer getBalance();
    // 取款
    void withdraw(Integer amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作 * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demoTest(Account account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            join(t);
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance() + " cost: " + (end - start) / 1000_000 + " ms");
    }
}
