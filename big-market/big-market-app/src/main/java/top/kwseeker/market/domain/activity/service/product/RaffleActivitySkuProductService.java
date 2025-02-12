package top.kwseeker.market.domain.activity.service.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import top.kwseeker.market.domain.activity.adapter.repository.IActivityRepository;
import top.kwseeker.market.domain.activity.model.entity.SkuProductEntity;
import top.kwseeker.market.domain.activity.service.IRaffleActivitySkuProductService;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description sku商品服务
 * @create 2024-06-15 09:30
 */
@ApplicationScoped
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {

    @Inject
    IActivityRepository repository;

    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return repository.querySkuProductEntityListByActivityId(activityId);
    }

}
