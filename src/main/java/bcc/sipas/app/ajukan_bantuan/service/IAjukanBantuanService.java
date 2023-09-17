package bcc.sipas.app.ajukan_bantuan.service;

import bcc.sipas.dto.AjukanBantuanDto;
import bcc.sipas.entity.AjukanBantuan;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAjukanBantuanService {
    Mono<ResponseEntity<Response<AjukanBantuan>>> create(Long ortuId, AjukanBantuanDto.Create dto);
    Mono<ResponseEntity<Response<List<AjukanBantuan>>>> getListOrtu(Long ortuId, Pageable pageable);
    Mono<ResponseEntity<Response<List<AjukanBantuan>>>> getListFaskes(Long faskesId, String statusAjuan, Pageable pageable);
    Mono<ResponseEntity<Response<AjukanBantuan>>> get(Long id);
    Mono<ResponseEntity<Response<AjukanBantuan>>> update(Long id, AjukanBantuanDto.Update dto);
}
