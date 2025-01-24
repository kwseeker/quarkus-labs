package top.kwseeker.quarkus.extension.redisson.runtime;

import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class RedissonClientRecorder {

    public void createProducer() {
        Arc.container().instance(RedissonClientProducer.class).get();
    }
}
