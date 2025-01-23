package top.kwseeker.market.infrastructure.quarkus;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.AnnotationLiteral;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CDIBeanSelector {

    public static <T> T select(Instance<T> instance, String named) {
        Optional<T> selectedBean = instance.select(NamedLiteral.of(named)).stream()
                .findFirst();
        if (selectedBean.isEmpty()) {
            log.error("cannot find bean with name {}", named);
            throw new RuntimeException("cannot find bean with name " + named);
        }
        return selectedBean.get();
    }

    public static <T> T select(Class<T> clazz, String named) {
        Optional<T> selectedBean = CDI.current().select(clazz, NamedLiteral.of(named)).stream()
                .findFirst();
        if (selectedBean.isEmpty()) {
            log.error("cannot find bean of type {} named {}", clazz, named);
            throw new RuntimeException("cannot find bean with name " + named);
        }
        return selectedBean.get();
    }

    public static <T> Map<String, T> selectMap(Class<T> clazz) {
        BeanManager beanManager = CDI.current().getBeanManager();
        Map<String, T> selectedBeanMap = beanManager.getBeans(clazz).stream()
                .map(bean -> {
                    CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
                    T instance = (T) beanManager.getReference(bean, clazz, ctx);
                    return Map.entry(bean.getName(), instance);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (selectedBeanMap.isEmpty()) {
            log.warn("cannot find bean of type {}", clazz);
        }
        return selectedBeanMap;
    }
}
