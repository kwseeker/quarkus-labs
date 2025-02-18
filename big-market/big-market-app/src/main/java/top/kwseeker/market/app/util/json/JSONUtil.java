package top.kwseeker.market.app.util.json;

import com.fasterxml.jackson.core.type.TypeReference;

public class JSONUtil {

    private static final JSON jsonAdapter = new JacksonAdapter();

    public static String toJSONString(Object object) {
        return jsonAdapter.toJSONString(object);
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        return jsonAdapter.parseObject(text, typeReference);
    }
}
