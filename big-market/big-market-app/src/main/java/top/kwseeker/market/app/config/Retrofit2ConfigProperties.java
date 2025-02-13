package top.kwseeker.market.app.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

/**
 * <a href="https://cn.quarkus.io/guides/config-mappings">Quarkus将配置映射到对象</a>
 */
@StaticInitSafe
@ConfigMapping(prefix = "gateway.config")
public interface Retrofit2ConfigProperties {

    /** 状态；open = 开启、close 关闭 */
    boolean enable();
    /** 转发地址 */
    String apiHost();

    /** 大营销接口调用配置，注意 Quarkus 嵌套配置必须提供此返回内部配置接口实例的方法 */
    BigMarket bigMarket();

    interface BigMarket {

        String appId();

        String appToken();
    }
}