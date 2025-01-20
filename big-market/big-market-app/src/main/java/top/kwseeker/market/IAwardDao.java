package top.kwseeker.market;

import org.apache.ibatis.annotations.Mapper;
//import top.kwseeker.market.infrastructure.dao.po.Award;

import java.util.List;

@Mapper
public interface IAwardDao {

    List<Award> queryAwardList();

    String queryAwardConfigByAwardId(Integer awardId);

    String queryAwardKeyByAwardId(Integer awardId);

}
