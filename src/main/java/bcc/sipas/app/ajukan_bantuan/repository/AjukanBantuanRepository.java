package bcc.sipas.app.ajukan_bantuan.repository;

import bcc.sipas.app.orang_tua_faskes.repository.OrangtuaFaskesRepository;
import bcc.sipas.entity.AjukanBantuan;
import bcc.sipas.entity.OrangtuaFaskes;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.mapper.AjukanBantuanMapper;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AjukanBantuanRepository {

    @Autowired
    private IAjukanBantuanRepository repository;

    @Autowired
    private OrangtuaFaskesRepository ortuFaskesRepository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<AjukanBantuan> create(AjukanBantuan ajukanBantuan){
        return this.repository.save(ajukanBantuan);
    }

    public Mono<Page<AjukanBantuan>> getListFaskes(Long faskesId, String statusAjuan, Pageable pageable){
        return this.ortuFaskesRepository
                .getList(faskesId, Pageable.unpaged())
                .map(Page::getContent)
                .map(v -> v.stream().parallel().map(OrangtuaFaskes::getFkOrtuId).toList())
                .flatMap((fkOrtuIds) -> {
                    Query query = Query.query(
                            Criteria.where("fk_ortu_id").in(fkOrtuIds)
                                    .and(
                                            Criteria.where("deleted_at").isNull()
                                    )
                                    .and(
                                            Criteria.where("status").is(statusAjuan)
                                    )
                    ).with(pageable);
                    return this.template
                            .select(query, AjukanBantuan.class)
                            .collectList();
                })
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data ajukan bantuan tidak ditemukan")))
                .zipWith(this.repository.count())
                .map((list) -> new PageImpl<>(list.getT1(), pageable, list.getT2()));
    }

    public Mono<Page<AjukanBantuan>> getListOrtu(Long ortuId, Pageable pageable){
        Query query = Query.query(
                Criteria.where("fk_ortu_id").is(ortuId)
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        ).with(pageable);
        return this.template
                .select(query, AjukanBantuan.class)
                .collectList()
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data ajukan bantuan tidak ditemukan")))
                .zipWith(this.repository.count())
                .map((l) -> new PageImpl<>(l.getT1(), pageable, l.getT2()));
    }

    public Mono<AjukanBantuan> getById(Long id){
        return this.repository.findById(id).switchIfEmpty(Mono.error(new DataTidakDitemukanException("data ajukan bantuan tidak ditemukan")));
    }

    public Mono<AjukanBantuan> update(Long id, AjukanBantuan ajukanBantuan){
        return this.repository
                .findById(id)
                .map((v) -> AjukanBantuanMapper.INSTANCE.updateAjukanBantuan(ajukanBantuan, v))
                .flatMap((v) -> this.repository.save(v));
    }

    public Mono<List<AjukanBantuan>> count(Long faskesId){
        return this.ortuFaskesRepository
                .getList(faskesId, Pageable.unpaged())
                .map(Page::getContent)
                .map((res) -> res.stream().parallel().map(OrangtuaFaskes::getFkOrtuId).toList())
                .flatMap((ortuIds) -> {
                    Query query = Query.query(
                            Criteria.where("fk_ortu_id").in(ortuIds)
                                    .and(
                                            Criteria.where("deleted_at").isNull()
                                    )
                    );
                    return this.template
                            .select(query, AjukanBantuan.class)
                            .collectList();
                });
    }
}
