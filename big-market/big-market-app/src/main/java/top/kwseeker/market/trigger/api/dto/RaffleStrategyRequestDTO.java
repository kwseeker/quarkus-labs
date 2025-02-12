package top.kwseeker.market.trigger.api.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖请求参数
 * @create 2024-02-14 17:26
 */
@Data
public class RaffleStrategyRequestDTO implements Serializable {

    // 抽奖策略ID
    @QueryParam("strategyId")
    private Long strategyId;

}
