package bcc.sipas.app.data_anak.service;

import bcc.sipas.app.data_anak.repository.DataAnakRepository;
import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.dto.DataAnakDto;
import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.Response;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.exception.DatabaseException;
import bcc.sipas.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@Slf4j
public class DataAnakService implements IDataAnakService{

    @Autowired
    private DataAnakRepository repository;

    @Autowired
    private OrangtuaRepository orangtuaRepository;

    @Override
    public Mono<ResponseEntity<Response<DataAnak>>> create(Long id, DataAnakDto.Create dto) {
        DataAnak dataAnak = dto.toDataAnak();
        dataAnak.setFkOrtuId(id);
        return this.orangtuaRepository
                .findOne(
                        Example.of(Orangtua
                                .builder()
                                .id(id)
                                .build())
                )
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orang tua tidak ditemukan")))
                .flatMap((d) -> {
                    if(d.getIsConnectedWithFaskes() == null){
                        return Mono.error(new DatabaseException("pastikan orangtua sudah terhubung dengan fasilitas kesehatan"));
                    }
                    if(d.getIsConnectedWithFaskes()){
                        return Mono.just(d);
                    } else {
                        return Mono.error(new DatabaseException("pastikan orangtua sudah terhubung dengan fasilitas kesehatan"));
                    }
                })
                .then(this.repository.save(dataAnak))
                .doOnError((e) -> {
                    log.error("error occurred with message {}", e.getMessage());
                })
                .flatMap((d) -> Mono.fromCallable(() -> ResponseUtil
                        .sendResponse(HttpStatus.CREATED,
                                Response
                                        .<DataAnak>builder()
                                        .data(d)
                                        .message("sukses membuat data anak")
                                        .success(true)
                                        .build()
                        ))
                );
    }
}
