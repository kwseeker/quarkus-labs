package top.kwseeker.market.infrastructure.dao.po;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动账户表-月次数
 * @create 2024-04-03 15:28
 */
public class RaffleActivityAccountMonth {

    private final static SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");

    /** 自增ID */
    private Long id;
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 月（yyyy-mm） */
    private String month;
    /** 月次数 */
    private Integer monthCount;
    /** 月次数-剩余 */
    private Integer monthCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

    public static String currentMonth() {
        return dateFormatMonth.format(new Date());
    }

    public RaffleActivityAccountMonth() {
    }

    public RaffleActivityAccountMonth(Long id, String userId, Long activityId, String month, Integer monthCount, Integer monthCountSurplus, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.month = month;
        this.monthCount = monthCount;
        this.monthCountSurplus = monthCountSurplus;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }

    public Integer getMonthCountSurplus() {
        return monthCountSurplus;
    }

    public void setMonthCountSurplus(Integer monthCountSurplus) {
        this.monthCountSurplus = monthCountSurplus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
