package com.datou.n6.section3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.datou.util.ThreadUtil.join;

public class Section3_1 {
    public static void main(String[] args) {
        DecimalAccount.demo(new DecimalAccountUnsafe(new BigDecimal("10000")));
        DecimalAccount.demo(new DecimalAccountSafeLock(new BigDecimal("10000")));
        DecimalAccount.demo(new DecimalAccountSafeCas(new BigDecimal("10000")));
    }
}

// 线程安全 -> CAS
class DecimalAccountSafeCas implements DecimalAccount{
    private AtomicReference<BigDecimal> balance;
    public DecimalAccountSafeCas(BigDecimal balance) {
        this.balance = new AtomicReference<>(balance);
    }
    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }
    @Override
    public void withdraw(BigDecimal amount) {
        while (true){
            BigDecimal prev = balance.get();
            BigDecimal cur = prev.subtract(amount);
            if (balance.compareAndSet(prev, cur)){
                break;
            }
        }
    }
}

// 线程安全 -> 加锁
class DecimalAccountSafeLock implements DecimalAccount{
    private BigDecimal balance;
    public DecimalAccountSafeLock(BigDecimal balance) {
        this.balance = balance;
    }
    @Override
    public synchronized BigDecimal getBalance() {
        return balance;
    }
    @Override
    public synchronized void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
}

// 线程不安全
class DecimalAccountUnsafe implements DecimalAccount{
    private BigDecimal balance;
    public DecimalAccountUnsafe(BigDecimal balance) {
        this.balance = balance;
    }
    @Override
    public BigDecimal getBalance() {
        return balance;
    }
    @Override
    public void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
}

interface DecimalAccount {
    BigDecimal getBalance(); // 获取余额
    void withdraw(BigDecimal amount);// 取款
    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(DecimalAccount account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
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
