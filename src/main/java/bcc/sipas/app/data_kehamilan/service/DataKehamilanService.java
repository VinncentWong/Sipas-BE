package bcc.sipas.app.data_kehamilan.service;

import bcc.sipas.app.data_kehamilan.repository.DataKehamilanRepository;
import bcc.sipas.app.orang_tua_faskes.repository.OrangtuaFaskesRepository;
import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.app.pemeriksaan_kehamilan.repository.PemeriksaanKehamilanRepository;
import bcc.sipas.dto.DataKehamilanDto;
import bcc.sipas.entity.*;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.exception.DatabaseException;
import bcc.sipas.util.CollectionUtils;
import bcc.sipas.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DataKehamilanService implements IDataKehamilanService{

    @Autowired
    private DataKehamilanRepository repository;

    @Autowired
    private OrangtuaRepository ortuRepository;

    @Autowired
    private OrangtuaFaskesRepository ortuFaskesRepository;

    @Autowired
    private PemeriksaanKehamilanRepository pemeriksaanKehamilanRepository;

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

    @Override
    public Mono<ResponseEntity<Response<DataKehamilan>>> get(Long id) {
        return this.repository
                .find(Example
                        .of(DataKehamilan
                                .builder()
                                .id(id)
                                .build())
                )
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data kehamilan tidak ditemukan")))
                .flatMap((d) -> Mono.fromCallable(() -> ResponseUtil
                        .sendResponse(HttpStatus.OK,
                                Response
                                        .<DataKehamilan>builder()
                                        .data(d)
                                        .success(true)
                                        .message("sukses mendapatkan data kehamilan")
                                        .build()))
                );
    }

    public Mono<ResponseEntity<Response<Map<String, Long>>>> count(Long faskesId){
        return this.ortuFaskesRepository
                .getList(faskesId, Pageable.unpaged())
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua faskes tidak ditemukan")))
                .map(Page::getContent)
                .map((d) -> d.stream().parallel().map(OrangtuaFaskes::getFkOrtuId).toList())
                .flatMap((ortuIds) -> {
                    Mono<List<DataKehamilan>> dataKehamilan = this.repository
                            .count(ortuIds, faskesId);
                    return dataKehamilan
                            .map((v) -> {
                                var dataKehamilanIds = v.stream().parallel().map(DataKehamilan::getId).collect(Collectors.toList());
                                return this
                                        .pemeriksaanKehamilanRepository
                                        .count(dataKehamilanIds)
                                        .map((dataPemeriksaanKehamilans) -> ResponseUtil
                                                .sendResponse(
                                                        HttpStatus.OK,
                                                        Response
                                                                .<Map<String, Long>>builder()
                                                                .success(true)
                                                                .message("sukses mendapatkan data statistik ibu hamil")
                                                                .data(
                                                                        CollectionUtils.ofLinkedHashMap(
                                                                                new String[]{"jumlahProfilCalonBayi", "jumlahSudahPeriksa", "jumlahBelumPeriksa"},
                                                                                new Long[]{Integer.toUnsignedLong(v.size()), Integer.toUnsignedLong(dataPemeriksaanKehamilans.size()), Integer.toUnsignedLong(v.size() - dataPemeriksaanKehamilans.size())}
                                                                        )
                                                                )
                                                                .build()
                                                ));
                            });
                })
                .flatMap((v) -> v)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
