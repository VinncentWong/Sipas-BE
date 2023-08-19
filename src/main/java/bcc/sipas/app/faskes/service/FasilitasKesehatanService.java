package bcc.sipas.app.faskes.service;

import bcc.sipas.app.faskes.repository.FasilitasKesehatanRepository;
import bcc.sipas.dto.FasilitasKesehatanDto;
import bcc.sipas.entity.FasilitasKesehatan;
import bcc.sipas.entity.Response;
import bcc.sipas.exception.EmailSudahAdaException;
import bcc.sipas.exception.KredensialTidakValidException;
import bcc.sipas.security.authentication.JwtAuthentication;
import bcc.sipas.util.BcryptUtil;
import bcc.sipas.util.JwtUtil;
import bcc.sipas.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class FasilitasKesehatanService implements IFasilitasKesehatanService {

    @Autowired
    private FasilitasKesehatanRepository fasilitasKesehatanRepository;

    public Mono<ResponseEntity<Response<FasilitasKesehatan>>> create(FasilitasKesehatanDto.Create dto){
        FasilitasKesehatan faskes = dto.toFaskes();
        faskes.setKodeUnik(UUID.randomUUID().toString());
        return this
                .fasilitasKesehatanRepository.findByEmail(faskes.getEmail())
                .flatMap((f) -> Mono.error(new EmailSudahAdaException("email sudah terdaftar di database")))
                .then(fasilitasKesehatanRepository.save(faskes))
                .flatMap((f) -> Mono.fromCallable(() -> ResponseUtil
                            .sendResponse(
                                    HttpStatus.CREATED,
                                    Response
                                            .<FasilitasKesehatan>builder()
                                            .success(true)
                                            .message("sukses membuat fasilitas kesehatan")
                                            .data(f)
                                            .build())
                    )
                );
    }

    @Override
    public Mono<ResponseEntity<Response<FasilitasKesehatan>>> login(FasilitasKesehatanDto.Login dto) {
        return this.fasilitasKesehatanRepository
                .findByEmail(dto.email())
                .switchIfEmpty(Mono.error(new KredensialTidakValidException("kredensial tidak valid")))
                .flatMap((f) -> Mono.defer(
                        () -> {
                            if(BcryptUtil.match(dto.password(), f.getPassword())){
                                JwtAuthentication<Long> jwtAuthentication = new JwtAuthentication<>(f.getUsername(), f.getEmail());
                                jwtAuthentication.setId(f.getId());
                                String token = JwtUtil.generateToken(jwtAuthentication, "ROLE_FASKES");
                                return Mono.fromCallable(() -> ResponseUtil.<FasilitasKesehatan>sendResponse(
                                        HttpStatus.CREATED,
                                        Response
                                                .<FasilitasKesehatan>builder()
                                                .message("faskes sukses login")
                                                .jwtToken(token)
                                                .data(f)
                                                .success(true)
                                                .build()
                                ));
                            } else {
                                return Mono.error(new KredensialTidakValidException("kredensial tidak valid"));
                            }
                        }
                ));
    }
}
