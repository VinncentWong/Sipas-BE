package bcc.sipas.app.data_anak.service;

import bcc.sipas.dto.DataAnakDto;
import bcc.sipas.dto.DataPemeriksaanAnakDto;
import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.DataAnakOrtu;
import bcc.sipas.entity.DataPemeriksaanAnak;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IDataAnakService {

    Mono<ResponseEntity<Response<DataAnak>>> create(Long id, DataAnakDto.Create dto);
    Mono<ResponseEntity<Response<List<DataAnak>>>> getList(Long id, Pageable pageable);
    Mono<ResponseEntity<Response<Map<String, Long>>>> count(Long faskesId);

    Mono<ResponseEntity<Response<DataAnakOrtu>>> getList(Long faskesId, DataAnakDto.SearchByName dto, Pageable pageable);
}
