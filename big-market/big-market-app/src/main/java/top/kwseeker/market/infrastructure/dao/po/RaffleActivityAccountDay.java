package top.kwseeker.market.infrastructure.dao.po;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动账户表-日次数
 * @create 2024-04-03 15:28
 */
public class RaffleActivityAccountDay {

    private final static SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 自增ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 日期（yyyy-mm-dd）
     */
    private String day;
    /**
     * 日次数
     */
    private Integer dayCount;
    /**
     * 日次数-剩余
     */
    private Integer dayCountSurplus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public static String currentDay() {
        return dateFormatDay.format(new Date());
    }

    public RaffleActivityAccountDay() {
    }

    public RaffleActivityAccountDay(Long id, String userId, Long activityId, String day, Integer dayCount, Integer dayCountSurplus, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.day = day;
        this.dayCount = dayCount;
        this.dayCountSurplus = dayCountSurplus;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getDayCount() {
        return dayCount;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public Integer getDayCountSurplus() {
        return dayCountSurplus;
    }

    public void setDayCountSurplus(Integer dayCountSurplus) {
        this.dayCountSurplus = dayCountSurplus;
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
