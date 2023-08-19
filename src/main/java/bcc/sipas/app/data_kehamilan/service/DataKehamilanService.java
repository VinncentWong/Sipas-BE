package bcc.sipas.app.data_kehamilan.service;

import bcc.sipas.app.data_kehamilan.repository.DataKehamilanRepository;
import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.dto.DataKehamilanDto;
import bcc.sipas.entity.DataKehamilan;
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
public class DataKehamilanService implements IDataKehamilanService{

    @Autowired
    private DataKehamilanRepository repository;

    @Autowired
    private OrangtuaRepository ortuRepository;

    @Override
    public Mono<ResponseEntity<Response<DataKehamilan>>> create(Long id, DataKehamilanDto.Create dto) {
        DataKehamilan dataKehamilan = dto.toDataKehamilan();
        dataKehamilan.setFkOrtuId(id);
        return this
                .ortuRepository.findOne(Example.of(
                        Orangtua
                                .builder()
                                .id(id)
                                .build()
                    )
                )
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")))
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
                .then(this.repository.save(dataKehamilan))
                .flatMap((d) -> Mono.fromCallable(() -> ResponseUtil
                        .sendResponse(
                                HttpStatus.CREATED,
                                Response
                                        .<DataKehamilan>builder()
                                        .message("sukses membuat data kehamilan")
                                        .data(d)
                                        .success(true)
                                        .build()
                        ))
                );
    }
}
