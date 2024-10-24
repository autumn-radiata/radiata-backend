package radiata.service.order.core.service;

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
}
