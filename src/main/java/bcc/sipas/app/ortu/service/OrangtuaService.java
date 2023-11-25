package bcc.sipas.app.ortu.service;

import bcc.sipas.app.faskes.repository.FasilitasKesehatanRepository;
import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.app.storage.repository.StorageRepository;
import bcc.sipas.constant.ImageConstant;
import bcc.sipas.dto.OrangtuaDto;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.ResepMakanan;
import bcc.sipas.entity.Response;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.exception.DatabaseException;
import bcc.sipas.exception.EmailSudahAdaException;
import bcc.sipas.exception.KredensialTidakValidException;
import bcc.sipas.mapper.OrangtuaMapper;
import bcc.sipas.security.authentication.JwtAuthentication;
import bcc.sipas.util.BcryptUtil;
import bcc.sipas.util.JwtUtil;
import bcc.sipas.util.ResponseUtil;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Service
@Transactional
@Slf4j
public class OrangtuaService implements IOrangtuaService{

    @Autowired
    private OrangtuaRepository repository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private FasilitasKesehatanRepository faskesRepository;

    @Override
    public Mono<ResponseEntity<Response<Orangtua>>> create(OrangtuaDto.Create dto) {
        Orangtua orangtua = dto.toOrangtua();
        return Mono
                .from(this.repository.findByEmail(orangtua.getEmail()))
                .flatMap((o) -> {
                    if(o != null){
                        return Mono.error(new EmailSudahAdaException("email sudah terdaftar"));
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
                    if (BcryptUtil.match(dto.password(), o.getPassword())) {
                        JwtAuthentication<Long> jwtAuthentication = new JwtAuthentication<>(String.format("%s-%s", o.getNamaAyah(), o.getNamaIbu()), o.getEmail());
                        jwtAuthentication.setId(o.getId());
                        String jwtToken = JwtUtil.generateToken(jwtAuthentication, "ROLE_ORANGTUA");
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
    public Mono<ResponseEntity<Response<Orangtua>>> get(Long id) {
        return this.repository
                .findById(id)
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")))
                .flatMap((d) -> Mono.fromCallable(() ->
                    ResponseUtil
                            .sendResponse(
                                    HttpStatus.OK,
                                    Response
                                            .<Orangtua>builder()
                                            .data(d)
                                            .success(true)
                                            .message("sukses menemukan data orangtua")
                                            .build()
                            )
                ));
    }

    @Override
    public Mono<ResponseEntity<Response<Orangtua>>> update(Long id, Orangtua orangtua, Mono<FilePart> image) {
        log.info("image = {}", image);
        return image
                .flatMap((f) -> Mono.fromCallable(() -> {
                    File file = File.createTempFile(ImageConstant.TEMP_FILE_PREFIX, f.filename());
                    return f.transferTo(file).then(Mono.just(file));
                }))
                .flatMap((v) -> v)
                .flatMap((f) -> Mono.fromCallable(() -> {
                    try {
                        byte[] bytes =  Files.readAllBytes(f.toPath());
                        f.delete();
                        return bytes;
                    } catch (IOException e) {
                        throw new DatabaseException(String.format("terjadi kesalahan pada saat membaca byte file dengan pesan sistem %s", e.getLocalizedMessage()));
                    }
                }))
                .flatMap((v) -> this.storageRepository.create(v))
                .zipWith(Mono.from(this.repository.findById(id)))
                .flatMap((t) -> {

                    var resp = t.getT1();
                    var ortu = t.getT2();

                    ortu.setImageUrl(resp.getSecureUrl());

                    ortu = OrangtuaMapper.INSTANCE.update(orangtua, ortu);

                    return this.repository.update(ortu);
                })
                .map((ortu) -> ResponseUtil
                        .sendResponse(
                                HttpStatus.CREATED,
                                Response.<Orangtua>builder()
                                        .message("sukses mengupdate data orangtua")
                                        .success(true)
                                        .data(ortu)
                                        .build()
                        )
                )
                .switchIfEmpty(
                        Mono.defer(() -> {
                            return this.repository
                                    .findById(id)
                                    .flatMap((data) -> {
                                        var ortu = data;

                                        ortu = OrangtuaMapper.INSTANCE.update(orangtua, ortu);

                                        return this.repository.update(ortu);
                                    })
                                    .map((ortu) -> ResponseUtil
                                            .sendResponse(
                                                    HttpStatus.CREATED,
                                                    Response.<Orangtua>builder()
                                                            .message("sukses mengupdate data orangtua")
                                                            .success(true)
                                                            .data(ortu)
                                                            .build()
                                            )
                                    );
                        })
                );
    }

}
