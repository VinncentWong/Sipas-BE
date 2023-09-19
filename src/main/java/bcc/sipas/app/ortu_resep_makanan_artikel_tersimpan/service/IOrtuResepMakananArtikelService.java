package bcc.sipas.app.ortu_resep_makanan_artikel_tersimpan.service;

import bcc.sipas.dto.ResepMakananArtikelTersimpanDto;
import bcc.sipas.entity.ResepMakananArtikelTersimpan;
import bcc.sipas.entity.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IOrtuResepMakananArtikelService {
    Mono<ResponseEntity<Response<ResepMakananArtikelTersimpan>>> create(Long ortuId, ResepMakananArtikelTersimpanDto.Create dto);
    Mono<ResponseEntity<Response<ResepMakananArtikelTersimpan>>> get(Long ortuId, Long resepMakananId, Long artikelId, String jenis);
    Mono<ResponseEntity<Response<Void>>> delete(Long ortuId, Long resepMakananId, Long artikelId, String jenis);
    Mono<ResponseEntity<Response<Void>>> activate(Long ortuId, Long resepMakananId, Long artikelId, String jenis);
    Mono<ResponseEntity<Response<List<ResepMakananArtikelTersimpan>>>> getList(Long ortuId, Pageable pageable);
}
