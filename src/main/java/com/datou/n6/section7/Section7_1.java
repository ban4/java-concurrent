package com.datou.n6.section7;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe
 *  提供了非常底层的，操作内存、线程的方法，Unsafe 对象不能直接调用，只能通过反射获得
 *  注意： 与是否安全 无关
 */
public class Section7_1 {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        Field var = Unsafe.class.getDeclaredField("theUnsafe");
        var.setAccessible(true);
        Unsafe unsafe = (Unsafe) var.get(null);
        System.out.println(unsafe);

        Field id = Student.class.getDeclaredField("id");
        Field name = Student.class.getDeclaredField("name");

        // 获得成员变量的偏移量
        long idOffset = unsafe.objectFieldOffset(id);
        long nameOffset = unsafe.objectFieldOffset(name);

        Student student = new Student();
        unsafe.compareAndSwapInt(student,idOffset,0,20);
        unsafe.compareAndSwapObject(student,nameOffset,null,"张三");
        System.out.println(student);
    }

}

@Data
class Student{
    volatile int id;
    volatile String name;
}
