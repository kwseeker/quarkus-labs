package top.kwseeker.market.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.kwseeker.market.infrastructure.dao.IAwardDao;
import top.kwseeker.market.infrastructure.dao.po.Award;

import java.util.List;

@QuarkusTest
public class AwardDaoTest {

    private static final Logger log = LoggerFactory.getLogger(AwardDaoTest.class);

    @Inject
    IAwardDao awardDao;

    @Test
    public void testAwardDao() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("awards: {}", awards);
        Assertions.assertFalse(awards.isEmpty());
    }
}
