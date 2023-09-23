package bcc.sipas.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.core.util.ObjectMapperFactory;
import lombok.SneakyThrows;
import org.aspectj.weaver.TypeFactory;

import java.util.List;

public class ObjectMapperUtils {

    public static final ObjectMapper mapper;

    static{
        mapper = ObjectMapperFactory
                .createJson()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }

    @SneakyThrows
    public static <T> T readValue(String str, Class<T> value){
        return mapper.readValue(str, value);
    }

    @SneakyThrows
    public static <T> List<T> readListValue(String str, Class<T> value){
        var factory = mapper.getTypeFactory();
        return mapper.readValue(str, factory.constructCollectionType(List.class, value));
    }

    @SneakyThrows
    public static String writeValueAsString(Object obj){
        return mapper.writeValueAsString(obj);
    }
}
