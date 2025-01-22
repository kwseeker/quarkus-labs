package top.kwseeker.market.domain.strategy.service.armory.algorithm;

import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.kwseeker.market.domain.strategy.repository.IStrategyRepository;

import java.security.SecureRandom;

/**
 * 抽奖算法抽象类
 */
public abstract class AbstractAlgorithm implements IAlgorithm {

    @Inject
    protected IStrategyRepository repository;

    protected final SecureRandom secureRandom = new SecureRandom();

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum Algorithm {
        O1("o1Algorithm"),
        OLogN("oLogNAlgorithm");

        private String key;
    }

}