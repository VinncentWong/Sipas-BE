package bcc.sipas.app.ortu.service;

import bcc.sipas.dto.OrangtuaDto;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IOrangtuaService {
     Mono<ResponseEntity<Response<Orangtua>>> create(OrangtuaDto.Create dto);
}
