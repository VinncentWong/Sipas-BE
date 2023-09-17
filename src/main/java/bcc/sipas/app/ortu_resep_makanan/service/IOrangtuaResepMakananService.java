package bcc.sipas.app.ortu_resep_makanan.service;

import bcc.sipas.entity.OrangtuaResepMakanan;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IOrangtuaResepMakananService {
    Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> create(Long ortuId, Long resepMakananId);
    Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> get(Long ortuId, Long resepMakananId);
    Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> update(Long ortuResepMakananId, boolean remove);
}
