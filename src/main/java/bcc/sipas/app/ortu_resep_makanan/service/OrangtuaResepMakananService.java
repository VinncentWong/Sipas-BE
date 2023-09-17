package bcc.sipas.app.ortu_resep_makanan.service;

import bcc.sipas.app.ortu_resep_makanan.repository.OrangtuaResepMakananRepository;
import bcc.sipas.entity.OrangtuaResepMakanan;
import bcc.sipas.entity.Response;
import bcc.sipas.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@Slf4j
public class OrangtuaResepMakananService implements IOrangtuaResepMakananService{

    @Autowired
    private OrangtuaResepMakananRepository repository;

    @Override
    public Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> create(Long ortuId, Long resepMakananId) {
        return this.repository
                .create(ortuId, resepMakananId)
                .map(orangtuaResepMakanan -> ResponseUtil.sendResponse(
                        HttpStatus.CREATED,
                        Response
                                .<OrangtuaResepMakanan>builder()
                                .success(true)
                                .data(orangtuaResepMakanan)
                                .message("sukses membuat data orangtua resep makanan")
                                .build()
                ));
    }

    @Override
    public Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> get(Long ortuId, Long resepMakananId) {
        return this
                .repository
                .get(ortuId, resepMakananId)
                .map(orangtuaResepMakanan -> ResponseUtil.sendResponse(
                        HttpStatus.CREATED,
                        Response
                                .<OrangtuaResepMakanan>builder()
                                .success(true)
                                .data(orangtuaResepMakanan)
                                .message("sukses membuat data orangtua resep makanan")
                                .build()
                ));
    }

    @Override
    public Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> update(Long ortuResepMakananId, boolean remove) {
        return this.repository
                .update(ortuResepMakananId, remove)
                .map((v) -> ResponseUtil.sendResponse(
                        HttpStatus.OK,
                        Response
                                .<OrangtuaResepMakanan>builder()
                                .message("sukses mengupdate data orangtua resep makanan")
                                .data(v)
                                .success(true)
                                .build()
                ));
    }
}
