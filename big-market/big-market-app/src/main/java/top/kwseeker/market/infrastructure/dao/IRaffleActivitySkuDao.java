package top.kwseeker.market.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.market.infrastructure.dao.po.RaffleActivitySku;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 商品sku dao
 * @create 2024-03-16 11:04
 */
@Mapper
public interface IRaffleActivitySkuDao {

    RaffleActivitySku queryActivitySku(Long sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    List<RaffleActivitySku> queryActivitySkuListByActivityId(Long activityId);

    List<Long> querySkuList();

}
