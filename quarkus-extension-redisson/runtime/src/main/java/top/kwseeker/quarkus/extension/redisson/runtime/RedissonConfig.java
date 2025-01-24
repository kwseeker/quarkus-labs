package top.kwseeker.quarkus.extension.redisson.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.Map;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus")
public interface RedissonConfig {

    /**
     * Common params
     *
     * @return params
     */
    @WithName("redisson")
    Map<String, String> params();

    /**
     * Single server params
     *
     * @return params
     */
    @WithName("redisson.single-server-config")
    Map<String, String> singleServerConfig();

    /**
     * Cluster servers params
     *
     * @return params
     */
    @WithName("redisson.cluster-servers-config")
    Map<String, String> clusterServersConfig();

    /**
     * Sentinel servers params
     *
     * @return params
     */
    @WithName("redisson.sentinel-servers-config")
    Map<String, String> sentinelServersConfig();

    /**
     * Replicated servers params
     *
     * @return params
     */
    @WithName("redisson.replicated-servers-config")
    Map<String, String> replicatedServersConfig();

    /**
     * Master and slave servers params
     *
     * @return params
     */
    @WithName("redisson.master-slave-servers-config")
    Map<String, String> masterSlaveServersConfig();

}
