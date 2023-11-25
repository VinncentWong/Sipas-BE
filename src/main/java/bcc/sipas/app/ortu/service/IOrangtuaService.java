package bcc.sipas.app.ortu.service;

import bcc.sipas.dto.OrangtuaDto;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface IOrangtuaService {
     Mono<ResponseEntity<Response<Orangtua>>> create(OrangtuaDto.Create dto);
     Mono<ResponseEntity<Response<Orangtua>>> login(OrangtuaDto.Login dto);
     Mono<ResponseEntity<Response<Orangtua>>> get(Long id);
     Mono<ResponseEntity<Response<Orangtua>>> update(Long id, Orangtua orangtua, Mono<FilePart> file);
}
