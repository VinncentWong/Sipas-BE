package bcc.sipas.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder
@Setter
@Getter
public final class Response<T>{

    private String message;
    private T data;
    private boolean success;
    private String jwtToken;
    private PaginationResult<T> pagination;

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
