package top.kwseeker.market.domain.strategy.service.armory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import top.kwseeker.market.domain.strategy.model.entity.StrategyAwardEntity;
import top.kwseeker.market.domain.strategy.service.armory.algorithm.AbstractAlgorithm;
import top.kwseeker.market.domain.strategy.service.armory.algorithm.IAlgorithm;
import top.kwseeker.market.infrastructure.quarkus.BeanNameSelector;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;

/**
 * 策略装配库(兵工厂)，负责初始化策略计算
 */
@Slf4j
@ApplicationScoped
public class StrategyArmoryDispatch extends AbstractStrategyAlgorithm {
    // 抽奖算法阈值，在多少范围内开始选择不同选择
    private static final Integer ALGORITHM_THRESHOLD_VALUE = 10000;

    // 抽奖策略算法
    @Inject
    @Any
    Instance<IAlgorithm> algorithms;
    //Quarkus 不支持 Map 注入, 可以使用 instance#select() 查找，这里使用这种方式，参考 BeanNameSelector
    //或者使用 @Produces 定义一个 Map 类型的 bean
    //private final Map<String, IAlgorithm> algorithmMap;

    @Override
    protected void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1. 概率最小值
        BigDecimal minAwardRate = minAwardRate(strategyAwardEntities);
        // 2. 概率范围值
        double rateRange = convert(BigDecimal.valueOf(minAwardRate.doubleValue()));
        // 3. 根据概率值范围选择算法
        if (rateRange <= ALGORITHM_THRESHOLD_VALUE) {
            IAlgorithm o1Algorithm = BeanNameSelector.select(algorithms, AbstractAlgorithm.Algorithm.O1.getKey());
            o1Algorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.O1.getKey());
        } else {
            IAlgorithm oLogNAlgorithm = BeanNameSelector.select(algorithms, AbstractAlgorithm.Algorithm.OLogN.getKey());
            oLogNAlgorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.OLogN.getKey());
        }
    }

    @Override
    protected Integer dispatchAlgorithm(String key) {
        String beanName = repository.queryStrategyArmoryAlgorithmFromCache(key);
        if (null == beanName)
            throw new RuntimeException("key " + key + " beanName is null");
        IAlgorithm algorithm = BeanNameSelector.select(algorithms, beanName);
        return algorithm.dispatchAlgorithm(key);
    }

}
