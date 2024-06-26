package com.kxindot.goblin.random;

import static com.kxindot.goblin.Objects.newTreeMap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.kxindot.goblin.test.JunitTests;

/**
 * @author ZhaoQingJiang
 */
public class RandomChooserTests extends JunitTests {

    int stc = 100000;
    
    @Test
    public void singleTheadTest() {
        WeightRandomChooser<String> random = new WeightRandomChooser<>();
        random.add("同学a", 0.1d);
        random.add("同学b", 0.13d);
        random.add("同学c", 0.12d);
        random.add("同学d", 0.315d);
        random.add("同学e", 0.28d);
        random.add("同学f", 0.055d);
        println("运行前:");
        println("项目\t权重");
        for (WeightItem<String> item : random.items()) {
            println("{}\t{}", item.getItem(), item.getWeight());
        }
        println("总计:\t{}", random.items().stream().mapToDouble(WeightItem::getWeight).sum());
        Map<String, AtomicInteger> statis = newTreeMap();
        long time = System.currentTimeMillis();
        for (int i = 0; i < stc; i++) {
            statis.computeIfAbsent(random.choose().getItem(), c -> new AtomicInteger()).incrementAndGet();
        }
        println("运行后:");
        println("总用时:\t{}毫秒", System.currentTimeMillis() - time);
        println("项目\t次数\t比率");
        double tr = 0;
        for (Map.Entry<String, AtomicInteger> entry : statis.entrySet()) {
            String k = entry.getKey();
            int c = entry.getValue().get();
            double r = c / (double) stc;
            tr += r;
            println("{}\t{}\t{}", k, c, r);
        }
        println("总计:\t{}\t{}", stc, tr);
    }
    
}
