package com.datou.n6.section4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.datou.util.ThreadUtil.join;

/**
 * 原子数组
 *  AtomicIntegerArray
 *  AtomicLongArray
 *  AtomicReferenceArray
 *
 * 原子数组 保证 数组里的元素 原子操作，从而保证线程安全性
 */
public class AtomicArray {

    public static void main(String[] args) {
        // 线程不安全
        demo(
            () -> new int[10],
            (array) -> array.length,
            (array, index) -> array[index]++,
            array -> System.out.println(Arrays.toString(array))
        );
        // 线程安全
        demo(
            ()-> new AtomicIntegerArray(10),
            (array) -> array.length(),
            (array, index) -> array.getAndIncrement(index),
            array -> System.out.println(array)
        );

    }

    /**
     * 统一测试方法
     *
     * 参数1，提供数组、可以是线程不安全数组或线程安全数组
     * 参数2，获取数组长度的方法
     * 参数3，自增方法，回传 array, index
     * 参数4，打印数组的方法
     *
     * @param arraySupplier     supplier 提供者 无中生有 ()->结果
     * @param lengthFun         function 函数 一个参数一个结果 (参数)->结果,    BiFunction (参数1,参数2)->结果
     * @param putConsumer
     * @param printConsumer     consumer 消费者 一个参数没结果 (参数)->void,  BiConsumer (参数1,参数2)->
     * @param <T>
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer) {

        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            // 每个线程对数组作 10000 次操作
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j % length);
                }
            }));
        }

        ts.forEach(t -> t.start()); // 启动所有线程
        ts.forEach(t -> {
            join(t);
        });
        // 等所有线程结束
        printConsumer.accept(array);
    }

}
