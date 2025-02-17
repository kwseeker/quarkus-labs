package top.kwseeker.market.infrastructure.dao;

import top.kwseeker.market.infrastructure.dao.po.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 任务表，发送MQ
 * @create 2024-04-03 15:57
 */
@Mapper
public interface ITaskDao {

    void insert(Task task);

    //@DBRouter //TODO
    void updateTaskSendMessageCompleted(Task task);

    //@DBRouter
    void updateTaskSendMessageFail(Task task);

    List<Task> queryNoSendMessageTaskList();

}
