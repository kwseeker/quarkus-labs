package top.kwseeker.market.app.util.json;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * JSON 序列化框架并没有一个统一的接口规范，这里使用 Fastjson 的接口
 */
public interface JSON {

    String toJSONString(Object object);

    /**
     * 这里类型使用 Class<T> 会丢失泛型信息，
     * 使用 TypeReference<T> 但是 TypeReference<T> 接口又不是 JDK 的接口无法适配
     */
    <T> T parseObject(String text, TypeReference<T> typeReference);

    // 需要其他后面再加 ...
}
