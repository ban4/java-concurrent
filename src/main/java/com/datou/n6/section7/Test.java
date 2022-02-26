package com.datou.n6.section7;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.datou.util.ThreadUtil.join;

/**
 * 使用自定义的 AtomicData 实现之前线程安全的原子整数 Account 实现
 */
public class Test {

    public static void main(String[] args) {
        AtomicData atomicData = new AtomicData(10002);

        List<Thread> ts = new ArrayList<>(); // 10个线程，每个减1000
        for (int i = 0; i < 10; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicData.decrease(1);
                }
            }));
        }
        ts.forEach(t -> t.start());
        ts.forEach(t -> {
            join(t);
        });

        System.out.println(atomicData.getData());
    }

}

class AtomicData{
    private volatile int data;
    static final Unsafe unsafe;
    static final long DATA_OFFSET;

    static {
        try {
            Field var  = Unsafe.class.getDeclaredField("theUnsafe");
            var.setAccessible(true);
            unsafe = (Unsafe) var.get(null);
            // data 属性在 DataContainer 对象中的偏移量，用于 Unsafe 直接访问该属性
            DATA_OFFSET = unsafe.objectFieldOffset(AtomicData.class.getDeclaredField("data"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public AtomicData(int data){
        this.data = data;
    }

    public void decrease(int amount){
        int oldValue;
        while (true){
            // 获取共享变量旧值，可以在这一行加入断点，修改 data 调试来加深理解
            oldValue = data;
            // cas 尝试修改 data 为 旧值 + amount，如果期间旧值被别的线程改了，返回 false
            if (unsafe.compareAndSwapInt(this, DATA_OFFSET, oldValue, oldValue - amount)){
                return;
            }
        }
    }

    public int getData(){
        return data;
    }

}

