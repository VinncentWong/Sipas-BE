package bcc.sipas.app.data_anak.service;

import bcc.sipas.dto.DataAnakDto;
import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IDataAnakService {

    Mono<ResponseEntity<Response<DataAnak>>> create(Long id, DataAnakDto.Create dto);
    Mono<ResponseEntity<Response<List<DataAnak>>>> getList(Long id, Pageable pageable);
}
