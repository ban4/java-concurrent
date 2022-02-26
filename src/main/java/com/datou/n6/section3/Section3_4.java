package com.datou.n6.section3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import static com.datou.util.ThreadUtil.sleep;

/**
 * AtomicMarkableReference
 *  不关心引用变量更改了几次，只单纯关心是否更改过
 */
@Slf4j
public class Section3_4 {
    public static void main(String[] args) {
        GarbageBag bag = new GarbageBag("装满垃圾");
        // 参数2 mark 可以看作一个标记，表示垃圾袋满了
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);

        log.debug("主线程 start...");
        GarbageBag prev = ref.getReference();
        log.debug(prev.toString());
        new Thread(() -> {
            log.debug("打扫卫生的线程 start...");
            bag.setDesc("空垃圾袋");
            while (!ref.compareAndSet(bag, bag, true, false)) {
            }
            log.debug(bag.toString());
        }).start();

        sleep(1);
        log.debug("主线程想换一只新垃圾袋?");
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
        log.debug("换了么?" + success);
        log.debug(ref.getReference().toString());
    }

}

class GarbageBag {
    String desc;
    public GarbageBag(String desc) {
        this.desc = desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    @Override
    public String toString() {
        return super.toString() + " " + desc;
    }
}
