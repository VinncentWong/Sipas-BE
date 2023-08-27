package bcc.sipas.app.pemeriksaan_kehamilan.service;

import bcc.sipas.app.pemeriksaan_kehamilan.repository.PemeriksaanKehamilanRepository;
import bcc.sipas.dto.DataPemeriksaanKehamilanDto;
import bcc.sipas.entity.DataPemeriksaanKehamilan;
import bcc.sipas.entity.PaginationResult;
import bcc.sipas.entity.Response;
import bcc.sipas.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PemeriksaanKehamilanService implements IPemeriksaanKehamilanService{

    @Autowired
    private PemeriksaanKehamilanRepository repository;

    @Override
    public Mono<ResponseEntity<Response<DataPemeriksaanKehamilan>>> create(Long ortuId, Long faskesId, Long dataKehamilanId, DataPemeriksaanKehamilanDto.Create dto) {
        return this.repository
                .save(ortuId, faskesId, dataKehamilanId, dto.toDataPemeriksaanKehamilan())
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

    @Override
    public Mono<ResponseEntity<Response<List<DataPemeriksaanKehamilan>>>> getList(Long ortuId, Long faskesId, Long dataKehamilanId, Pageable page) {
        return this.repository
                .getList(ortuId, faskesId, dataKehamilanId, page)
                .flatMap((d) -> Mono.fromCallable(() -> ResponseUtil.sendResponse(
                        HttpStatus.OK,
                        Response
                                .<List<DataPemeriksaanKehamilan>>builder()
                                .message("sukses mendapatkan data pemeriksaan kehamilan")
                                .success(true)
                                .data(d.getContent())
                                .pagination(
                                        PaginationResult
                                                .<List<DataPemeriksaanKehamilan>>builder()
                                                .currentPage(page.getPageNumber())
                                                .currentElement(d.getNumberOfElements())
                                                .totalPage(d.getTotalPages())
                                                .totalElement(d.getTotalElements())
                                                .build()
                                )
                                .build()
                )));
    }
}
