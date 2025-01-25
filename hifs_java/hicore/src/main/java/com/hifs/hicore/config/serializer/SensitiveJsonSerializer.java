package com.hifs.hicore.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hifs.hicore.annotation.Sensitive;
import com.hifs.hicore.core.domain.model.LoginUser;
import com.hifs.hicore.enums.DesensitizedType;
import com.hifs.hicore.utils.SecurityUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 数据脱敏序列化过滤
 */
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizedType desensitizedType;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (desensitization()) {
            gen.writeString(desensitizedType.desensitizer().apply(value));
        } else {
            gen.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        Sensitive annotation = property.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.desensitizedType = annotation.desensitizedType();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    /**
     * 是否需要脱敏处理
     */
    private boolean desensitization() {
        try {
            LoginUser securityUser = SecurityUtils.getLoginUser();
            // 管理员不脱敏
            return !securityUser.getUser().isAdmin();
        } catch (Exception e) {
            return true;
        }
    }
}
