package top.kwseeker.market.domain.award.service.distribute.impl;

import jakarta.inject.Inject;
import top.kwseeker.market.domain.award.adapter.port.IAwardPort;
import top.kwseeker.market.domain.award.adapter.repository.IAwardRepository;
import top.kwseeker.market.domain.award.model.entity.DistributeAwardEntity;
import top.kwseeker.market.domain.award.service.distribute.IDistributeAward;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description OpenAI 账户调额奖品
 * @create 2024-10-06 11:18
 */
@ApplicationScoped
@Named("openai_use_count")
public class OpenAIAccountAdjustQuotaAward implements IDistributeAward {

    @Inject
    IAwardPort port;
    @Inject
    IAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) throws Exception {
        // 奖品ID
        Integer awardId = distributeAwardEntity.getAwardId();
        // 查询奖品ID 「优先走透传的随机积分奖品配置」
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = repository.queryAwardConfig(awardId);
        }
        // 发奖接口
        port.adjustAmount(distributeAwardEntity.getUserId(), Integer.valueOf(awardConfig));
    }

}
