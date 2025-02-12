package top.kwseeker.market.domain.activity.service.stage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import top.kwseeker.market.domain.activity.adapter.repository.IActivityRepository;
import top.kwseeker.market.domain.activity.model.entity.RaffleActivityStageEntity;
import top.kwseeker.market.domain.activity.service.IRaffleActivityStageService;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动上架服务
 * @create 2024-10-26 17:52
 */
@ApplicationScoped
public class RaffleActivityStageService implements IRaffleActivityStageService {

    @Inject
    IActivityRepository repository;

    @Override
    public void appendStageActivity(String channel, String source, Long activityId) {
        repository.appendStageActivity(channel, source, activityId);
    }

    @Override
    public void updateStageActivity2Active(Long id) {
        repository.updateStageActivity2Active(id);
    }

    @Override
    public Long queryStageActivityId(String channel, String source) {
        return repository.queryStageActiveBySC(channel, source);
    }

    @Override
    public List<RaffleActivityStageEntity> queryStageActivityList() {
        return repository.queryStageActivityList();
    }

    @Override
    public Long queryStageActivity2ActiveById(Long id) {
        return repository.queryStageActivity2ActiveById(id);
    }

}
