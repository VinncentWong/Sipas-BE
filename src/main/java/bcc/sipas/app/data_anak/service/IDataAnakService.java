package bcc.sipas.app.data_anak.service;

import bcc.sipas.dto.DataAnakDto;
import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IDataAnakService {

    Mono<ResponseEntity<Response<DataAnak>>> create(Long id, DataAnakDto.Create dto);
}
