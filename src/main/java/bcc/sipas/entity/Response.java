package bcc.sipas.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder
@Setter
@Getter
@JsonIncludeProperties(
        {
                "message", "data", "success"
        }
)
public final class Response<T>{

    private String message;
    private T data;
    private boolean success;

    public Response<T> putMessage(String message){
        this.message = message;
        return this;
    }
    public Response<T> putData(T data){
        this.data = data;
        return this;
    }
    public Response<T> putSuccess(boolean success){
        this.success = success;
        return this;
    }
}
