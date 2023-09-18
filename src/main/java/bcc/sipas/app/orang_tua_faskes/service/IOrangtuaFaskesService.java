package bcc.sipas.app.orang_tua_faskes.service;

import bcc.sipas.dto.OrangtuaFaskesDto;
import bcc.sipas.entity.OrangtuaFaskes;
import bcc.sipas.entity.OrangtuaFaskesDescription;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IOrangtuaFaskesService {

    Mono<ResponseEntity<Response<OrangtuaFaskes>>> connectFaskes(Long id, String kodeUnik);
    Mono<ResponseEntity<Response<List<OrangtuaFaskes>>>> getList(Long faskesId, Pageable page);
    Mono<ResponseEntity<Response<OrangtuaFaskesDescription>>> getOrtuFaskes(Long ortuId);
}
