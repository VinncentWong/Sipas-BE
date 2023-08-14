package bcc.sipas.util;

import bcc.sipas.entity.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public ResponseEntity<Response> sendResponse(HttpStatus status, Response data){
        return ResponseEntity
                .status(status.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
}
