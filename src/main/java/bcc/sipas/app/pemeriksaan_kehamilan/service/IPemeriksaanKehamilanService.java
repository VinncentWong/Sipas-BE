package bcc.sipas.app.pemeriksaan_kehamilan.service;

import bcc.sipas.dto.DataPemeriksaanKehamilanDto;
import bcc.sipas.entity.DataPemeriksaanKehamilan;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPemeriksaanKehamilanService {
    Mono<ResponseEntity<Response<DataPemeriksaanKehamilan>>> create(Long ortuId, Long faskesId, Long dataKehamilanId, DataPemeriksaanKehamilanDto.Create dto);
    Mono<ResponseEntity<Response<List<DataPemeriksaanKehamilan>>>> getList(Long ortuId, Long faskesId, Long dataKehamilanId, Pageable page);
}
