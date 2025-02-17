package top.kwseeker.market.domain.activity.service.quota.rule;

import top.kwseeker.market.domain.activity.model.entity.ActivityCountEntity;
import top.kwseeker.market.domain.activity.model.entity.ActivityEntity;
import top.kwseeker.market.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 下单规则过滤接口
 * @create 2024-03-23 09:40
 */
public interface IActionChain extends IActionChainArmory {

    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity,
                   ActivityCountEntity activityCountEntity);

}
