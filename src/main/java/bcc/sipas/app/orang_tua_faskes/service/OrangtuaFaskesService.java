package bcc.sipas.app.orang_tua_faskes.service;

import bcc.sipas.app.faskes.repository.FasilitasKesehatanRepository;
import bcc.sipas.app.orang_tua_faskes.repository.OrangtuaFaskesRepository;
import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.dto.OrangtuaFaskesDto;
import bcc.sipas.entity.*;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
class OrangtuaFaskesService implements IOrangtuaFaskesService {

    @Autowired
    private OrangtuaRepository repository;

    @Autowired
    private OrangtuaFaskesRepository orangtuaFaskesRepository;

    @Autowired
    private FasilitasKesehatanRepository faskesRepository;

    @Override
    public Mono<ResponseEntity<Response<OrangtuaFaskes>>> connectFaskes(Long id, String kodeUnik) {
        Mono<Orangtua> orangtua = this.repository
                .findById(id)
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")))
                .flatMap((d) -> {
                    d.setIsConnectedWithFaskes(true);
                    return this.repository.update(d);
                });
        Mono<FasilitasKesehatan> faskes = this.faskesRepository
                .findOne(Example.of(FasilitasKesehatan
                                .builder()
                                .kodeUnik(kodeUnik)
                                .build()
                        )
                )
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data faskes tidak ditemukan")));
        return Mono.zip(orangtua, faskes)
                .flatMap((t) -> {
                    Orangtua ortuData = t.getT1();
                    FasilitasKesehatan faskesData = t.getT2();
                    return this.orangtuaFaskesRepository.create(
                            OrangtuaFaskes
                                .builder()
                                .fkFaskesId(faskesData.getId())
                                .fkOrtuId(ortuData.getId())
                                .createdAt(LocalDate.now())
                                .updatedAt(LocalDate.now())
                                .build()
                    );
                })
                .flatMap((d) ->
                        Mono.fromCallable(() -> ResponseUtil.sendResponse(
                                HttpStatus.CREATED,
                                Response
                                        .<OrangtuaFaskes>builder()
                                        .success(true)
                                        .data(d)
                                        .message("sukses menghubungkan data orangtua dengan faskes")
                                        .build()
                        )
                    )
                );
    }

    @Override
    public Flux<ResponseEntity<Response<List<OrangtuaFaskes>>>> getList(Long faskesId, Pageable page) {
        return this
                .orangtuaFaskesRepository
                .getList(faskesId, page)
                .flatMap((d) -> Mono.fromCallable(() ->
                        ResponseUtil
                        .sendResponse(
                                HttpStatus.OK,
                                Response
                                        .<List<OrangtuaFaskes>>builder()
                                        .message("sukses mendapatkan data koneksi orangtua faskes")
                                        .success(true)
                                        .data(d.getContent())
                                        .pagination(
                                                PaginationResult
                                                        .<List<OrangtuaFaskes>>builder()
                                                        .totalElement(d.getTotalElements())
                                                        .totalPage(d.getTotalPages())
                                                        .currentPage(page.getPageNumber())
                                                        .currentElement(d.getContent().size())
                                                        .build()
                                        )
                                        .build()
                        )
                    )
                );
    }
}
