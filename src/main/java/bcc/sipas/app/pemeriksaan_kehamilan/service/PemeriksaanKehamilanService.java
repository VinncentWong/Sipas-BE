package bcc.sipas.app.pemeriksaan_kehamilan.service;

import bcc.sipas.app.pemeriksaan_kehamilan.repository.PemeriksaanKehamilanRepository;
import bcc.sipas.dto.DataPemeriksaanKehamilanDto;
import bcc.sipas.entity.DataPemeriksaanKehamilan;
import bcc.sipas.entity.Response;
import bcc.sipas.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@Slf4j
public class PemeriksaanKehamilanService implements IPemeriksaanKehamilanService{

    @Autowired
    private PemeriksaanKehamilanRepository repository;

    @Override
    public Mono<ResponseEntity<Response<DataPemeriksaanKehamilan>>> create(Long ortuId, Long faskesId, DataPemeriksaanKehamilanDto.Create dto) {
        return this.repository
                .save(ortuId, faskesId, dto.toDataPemeriksaanKehamilan())
                .flatMap((d) -> Mono.fromCallable(() -> ResponseUtil
                        .sendResponse(
                                HttpStatus.CREATED,
                                Response.<DataPemeriksaanKehamilan>builder()
                                        .data(d)
                                        .message("sukses membuat data pemeriksaan kehamilan")
                                        .success(true)
                                        .build()
                        )));
    }
}
