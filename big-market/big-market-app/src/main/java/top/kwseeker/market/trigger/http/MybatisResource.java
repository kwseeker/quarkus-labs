package top.kwseeker.market.trigger.http;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import top.kwseeker.market.infrastructure.dao.IAwardDao;
import top.kwseeker.market.infrastructure.dao.po.Award;

import java.util.List;

@Path("/mybatis")
public class MybatisResource {

    @Inject
    IAwardDao awardDao;

    @Path("/award")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Award> getAwards() {
        List<Award> awards = awardDao.queryAwardList();
        System.out.println("award");
        return awards;
    }
}
