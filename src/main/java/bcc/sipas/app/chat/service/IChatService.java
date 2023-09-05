package bcc.sipas.app.chat.service;

import bcc.sipas.entity.OpenApiClientResponse;
import bcc.sipas.entity.OpenApiResponse;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IChatService {
    Mono<ResponseEntity<Response<OpenApiResponse>>> create(Long ortuId, String message);
    Mono<ResponseEntity<Response<OpenApiClientResponse>>> get(Long ortuId);
}
