package top.kwseeker.market.infrastructure.quarkus;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BeanNameSelector {

    public static <T> T select(Instance<T> instance, String named) {
        Optional<T> selectedBean = instance.select(NamedLiteral.of(named)).stream()
                .findFirst();
        if (selectedBean.isEmpty()) {
            log.error("cannot find bean with name {}", named);
            throw new RuntimeException("cannot find bean with name " + named);
        }
        return selectedBean.get();
    }
}
