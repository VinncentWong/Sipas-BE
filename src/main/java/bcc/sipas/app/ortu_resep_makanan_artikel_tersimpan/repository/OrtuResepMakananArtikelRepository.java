package bcc.sipas.app.ortu_resep_makanan_artikel_tersimpan.repository;

import bcc.sipas.entity.ResepMakananArtikelTersimpan;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class OrtuResepMakananArtikelRepository implements IOrtuResepMakananArtikelRepository{

    @Autowired
    private R2dbcEntityTemplate template;

    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<Long> create(ResepMakananArtikelTersimpan resepMakananArtikelTersimpan) {
        return this.databaseClient
                .sql(this.CREATE)
                .bind("$1", resepMakananArtikelTersimpan.getFkArtikelId())
                .bind("$2", resepMakananArtikelTersimpan.getFkOrtuId())
                .bind("$3", resepMakananArtikelTersimpan.getFkResepMakananId())
                .bind("$4", resepMakananArtikelTersimpan.getJenis())
                .fetch()
                .rowsUpdated();
    }

    @Override
    public Mono<ResepMakananArtikelTersimpan> get(Long ortuId, Long resepMakananId, Long artikelId, String jenis) {
        Query query = Query.query(
                Criteria.where("fk_ortu_id").is(ortuId)
                        .and(
                                Criteria.where("fk_resep_makanan_id").is(resepMakananId)
                        )
                        .and(
                                Criteria.where("fk_artikel_id").is(artikelId)
                        )
                        .and(
                                Criteria.where("jenis").is(jenis)
                        )
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        ).limit(1);
        return this.template
                .select(query, ResepMakananArtikelTersimpan.class)
                .collectList()
                .flatMap((list) -> {
                    if(list.isEmpty()){
                        return Mono.error(new DataTidakDitemukanException("data tersimpan tidak ditemukan"));
                    } else {
                        return Mono.just(list.get(0));
                    }
                });
    }

    @Override
    public Mono<Long> delete(Long ortuId, Long resepMakananId, Long artikelId, String jenis) {
        Query query = Query.query(
                Criteria.where("fk_ortu_id").is(ortuId)
                        .and(
                                Criteria.where("fk_resep_makanan_id").is(resepMakananId)
                        )
                        .and(
                                Criteria.where("fk_artikel_id").is(artikelId)
                        )
                        .and(
                                Criteria.where("jenis").is(jenis)
                        )
        );
        return this.template
                .update(query, Update.update("deleted_at", LocalDate.now()), ResepMakananArtikelTersimpan.class)
                .flatMap((l) -> {
                    if(l < 1){
                        return Mono.error(new DatabaseException("data tersimpan tidak ditemukan"));
                    } else {
                        return Mono.just(l);
                    }
                });
    }

    @Override
    public Mono<Long> activate(Long ortuId, Long resepMakananId, Long artikelId, String jenis) {
        Query query = Query.query(
                Criteria.where("fk_ortu_id").is(ortuId)
                        .and(
                                Criteria.where("fk_resep_makanan_id").is(resepMakananId)
                        )
                        .and(
                                Criteria.where("fk_artikel_id").is(artikelId)
                        )
                        .and(
                                Criteria.where("jenis").is(jenis)
                        )
        );
        return this.template
                .update(query, Update.update("deleted_at", null), ResepMakananArtikelTersimpan.class)
                .flatMap((l) -> {
                    if(l < 1){
                        return Mono.error(new DatabaseException("data tersimpan tidak ditemukan"));
                    } else {
                        return Mono.just(l);
                    }
                });
    }

    @Override
    public Mono<Page<ResepMakananArtikelTersimpan>> getList(Long ortuId, Pageable pageable) {
        Query query = Query.query(
                Criteria.where("fk_ortu_id").is(ortuId)
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        ).with(pageable);
        return this.template
                .select(query, ResepMakananArtikelTersimpan.class)
                .collectList()
                .zipWith(this.template.count(query, ResepMakananArtikelTersimpan.class))
                .map((l) -> new PageImpl<>(l.getT1(), pageable, l.getT2()));
    }
}
