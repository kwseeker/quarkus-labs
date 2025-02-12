package top.kwseeker.market.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖奖品列表，请求对象
 * @create 2024-02-14 09:46
 */
@Data
public class RaffleAwardListRequestDTO implements Serializable {

    // 用户ID
    //@QueryParam("userId")
    private String userId;
    // 抽奖活动ID
    //@QueryParam("activityId")
    private Long activityId;

}
