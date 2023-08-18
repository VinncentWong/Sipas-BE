package bcc.sipas.app.faskes.service;

import bcc.sipas.dto.FasilitasKesehatanDto;
import bcc.sipas.entity.FasilitasKesehatan;
import bcc.sipas.entity.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IFasilitasKesehatanService {

    Mono<ResponseEntity<Response<FasilitasKesehatan>>> create(FasilitasKesehatanDto.Create dto);
    Mono<ResponseEntity<Response<FasilitasKesehatan>>> login(FasilitasKesehatanDto.Login dto);
}
