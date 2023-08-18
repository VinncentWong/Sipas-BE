package bcc.sipas.app.ortu.service;

import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.dto.OrangtuaDto;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.Response;
import bcc.sipas.exception.EmailSudahAdaException;
import bcc.sipas.exception.KredensialTidakValidException;
import bcc.sipas.security.authentication.JwtAuthentication;
import bcc.sipas.util.BcryptUtil;
import bcc.sipas.util.JwtUtil;
import bcc.sipas.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Transactional
@Slf4j
public class OrangtuaService implements IOrangtuaService{

    @Autowired
    private OrangtuaRepository repository;

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

    @Override
    public Mono<ResponseEntity<Response<Orangtua>>> login(OrangtuaDto.Login dto) {
        return this.repository.findByEmail(dto.email())
                .switchIfEmpty(Mono.error(new KredensialTidakValidException("kredensial tidak valid - email tidak ditemukan")))
                .log()
                .flatMap((o) -> {
                    if (BcryptUtil.match(dto.password(), o.getPassword())){
                        JwtAuthentication<Long> jwtAuthentication = new JwtAuthentication<>(String.format("%s-%s", o.getNamaAyah(), o.getNamaIbu()), o.getEmail());
                        jwtAuthentication.setId(o.getId());
                        String jwtToken = JwtUtil.generateToken(jwtAuthentication);
                        return Mono.fromCallable(() -> ResponseUtil.sendResponse(
                                HttpStatus.OK,
                                Response.<Orangtua>builder()
                                        .message("kredensial valid")
                                        .data(o)
                                        .success(true)
                                        .jwtToken(jwtToken)
                                        .build())
                        );
                    } else {
                        return Mono.error(new KredensialTidakValidException("kredensial tidak valid"));
                    }
                });
    }

    @Override
    public Mono<ResponseEntity<Response<Orangtua>>> connectFaskes(OrangtuaDto.ConnectFaskes dto) {
        return null;
    }
}
