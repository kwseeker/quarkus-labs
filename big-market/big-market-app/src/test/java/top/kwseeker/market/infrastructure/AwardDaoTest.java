package top.kwseeker.market.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.kwseeker.market.Award;
import top.kwseeker.market.IAwardDao;
//import top.kwseeker.market.infrastructure.dao.IAwardDao;
//import top.kwseeker.market.infrastructure.dao.po.Award;

import java.util.List;

@QuarkusTest
public class AwardDaoTest {

    @Inject
    IAwardDao awardDao;

    @Test
    public void testAwardDao() {
        List<Award> awards = awardDao.queryAwardList();
        Assertions.assertFalse(awards.isEmpty());
    }
}
