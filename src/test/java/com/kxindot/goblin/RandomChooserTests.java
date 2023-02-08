package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.newTreeMap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.kxindot.goblin.random.WeightItem;
import com.kxindot.goblin.random.WeightRandomChooser;
import com.kxindot.goblin.testkit.JunitTests;

/**
 * @author ZhaoQingJiang
 */
public class RandomChooserTests extends JunitTests {

    int stc = 100000;
    
    @Override
    public void beforeEach() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterEach() {
        // TODO Auto-generated method stub
        
    }

    @Test
    public void singleTheadTest() {
        WeightRandomChooser<String> random = new WeightRandomChooser<>();
        random.add("同学a", 0.1d);
        random.add("同学b", 0.13d);
        random.add("同学c", 0.12d);
        random.add("同学d", 0.315d);
        random.add("同学e", 0.28d);
        random.add("同学f", 0.055d);
        logger.info("运行前:");
        logger.info("项目\t权重");
        for (WeightItem<String> item : random.items()) {
            logger.info("{}\t{}", item.getItem(), item.getWeight());
        }
        logger.info("总计:\t{}", random.items().stream().mapToDouble(WeightItem::getWeight).sum());
        Map<String, AtomicInteger> statis = newTreeMap();
        long time = System.currentTimeMillis();
        for (int i = 0; i < stc; i++) {
            statis.computeIfAbsent(random.choose().getItem(), c -> new AtomicInteger()).incrementAndGet();
        }
        logger.info("运行后:");
        logger.info("总用时:\t{}毫秒", System.currentTimeMillis() - time);
        logger.info("项目\t次数\t比率");
        double tr = 0;
        for (Map.Entry<String, AtomicInteger> entry : statis.entrySet()) {
            String k = entry.getKey();
            int c = entry.getValue().get();
            double r = c / (double) stc;
            tr += r;
            logger.info("{}\t{}\t{}", k, c, r);
        }
        logger.info("总计:\t{}\t{}", stc, tr);
    }
    
}
