package radiata.service.user.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventSerializer {

    private static final Logger logger = LoggerFactory.getLogger(EventSerializer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().
        registerModule(new ParameterNamesModule());

    // 직렬화 (객체 -> JSON 문자열)
    public static <T> String serialize(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Failed to serialize object: {}", object, e);
            throw new RuntimeException("Serialization error", e);
        }
    }

    // 역직렬화 (JSON 문자열 -> 객체)
    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Failed to deserialize JSON: {}", json, e);
            throw new RuntimeException("Deserialization error", e);
        }
    }
}
