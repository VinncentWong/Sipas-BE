package bcc.sipas.app.pemeriksaan_kehamilan.service;

import bcc.sipas.dto.DataPemeriksaanKehamilanDto;
import bcc.sipas.entity.DataPemeriksaanKehamilan;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IPemeriksaanKehamilanService {
    Mono<ResponseEntity<Response<DataPemeriksaanKehamilan>>> create(Long ortuId, Long faskesId, DataPemeriksaanKehamilanDto.Create dto);
}
