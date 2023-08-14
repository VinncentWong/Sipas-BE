package bcc.sipas.entity;

import lombok.Builder;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder
public class Response extends LinkedHashMap<String, Object> {
    private Map<String, Object> map = new LinkedHashMap<>();
    public Response putData(String key, Object data){
        this.map.put(key, data);
        return this;
    }
}
