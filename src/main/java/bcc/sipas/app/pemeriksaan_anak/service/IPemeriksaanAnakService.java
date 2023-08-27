package bcc.sipas.app.pemeriksaan_anak.service;

import bcc.sipas.dto.DataPemeriksaanAnakDto;
import bcc.sipas.entity.DataPemeriksaanAnak;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPemeriksaanAnakService {
    Mono<ResponseEntity<Response<DataPemeriksaanAnak>>> create(Long ortuId, Long faskesId, Long dataAnakId, DataPemeriksaanAnakDto.Create dto);
    Mono<ResponseEntity<Response<List<DataPemeriksaanAnak>>>> getList(Long ortuId, Long faskesId, Long dataAnakId, Pageable page);
}
