package bcc.sipas.app.ortu.service;

import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.dto.OrangtuaDto;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.Response;
import bcc.sipas.exception.EmailSudahAdaException;
import bcc.sipas.util.ResponseUtil;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Transactional
@Slf4j
public class OrangtuaService implements IOrangtuaService{

    @Autowired
    private OrangtuaRepository repository;

    @Autowired
    private PostgresqlConnectionFactory factory;

    @Override
    public Mono<ResponseEntity<Response<Orangtua>>> create(OrangtuaDto.Create dto) {
        Orangtua orangtua = dto.toOrangtua();
        return Mono
                .from(this.repository.findByEmail(orangtua.getEmail()))
                .flatMap((o) -> {
                    if(o != null){
                        throw new EmailSudahAdaException("email sudah terdaftar");
                    }
                    return Mono.just(orangtua);
                })
                .then(this.repository.save(orangtua))
                .map((o) -> ResponseUtil.<Orangtua>sendResponse(
                            HttpStatus.CREATED,
                                Response
                                    .<Orangtua>builder()
                                    .build()
                                    .putMessage("sukses menyimpan data orang tua")
                                    .putData(o)
                                    .putSuccess(true)
                )
                )
                .onErrorResume((d) -> {
                    throw (RuntimeException)d;
                });
    }

    private Mono<Orangtua> executeCreate(Orangtua orangtua){
        orangtua.setCreatedAt(LocalDate.now());
        return Mono.from(factory
                .create()
                .flatMapMany((c) -> c.createStatement(OrangtuaRepository.createSql)
                        .bind("$1", orangtua.getNamaIbu())
                        .bind("$2", orangtua.getNamaAyah())
                        .bind("$3", orangtua.getEmail())
                        .bind("$4", orangtua.getPassword())
                        .bind("$5", orangtua.isConnectedWithFaskes())
                        .bind("$6", orangtua.getCreatedAt())
                        .returnGeneratedValues("id")
                        .execute())
                .flatMap((res) -> Mono.from(res.map((row, metadata) -> (Long)row.get("id"))))
                .flatMap((res) -> {
                    orangtua.setId(res);
                    return Mono.just(orangtua);
                })
        );
    }
}
