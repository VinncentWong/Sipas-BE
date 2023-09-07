package bcc.sipas.app.resep_makanan.service;

import bcc.sipas.dto.ResepMakananDto;
import bcc.sipas.entity.ResepMakanan;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IResepMakananService {
    Mono<ResponseEntity<Response<ResepMakanan>>> create(Long faskesId, ResepMakananDto.Create dto, Mono<FilePart> image);
    Mono<ResponseEntity<Response<List<ResepMakanan>>>> getList(Long id, Pageable pageable);
    Mono<ResponseEntity<Response<Void>>> delete(Long id);
    Mono<ResponseEntity<Response<Long>>> count(Long faskesId);
}
