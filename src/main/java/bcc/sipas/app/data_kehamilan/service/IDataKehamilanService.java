package bcc.sipas.app.data_kehamilan.service;

import bcc.sipas.dto.DataKehamilanDto;
import bcc.sipas.entity.DataKehamilan;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IDataKehamilanService {

    Mono<ResponseEntity<Response<DataKehamilan>>> create(Long id, DataKehamilanDto.Create dto);
}
