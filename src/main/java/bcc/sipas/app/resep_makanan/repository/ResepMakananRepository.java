package bcc.sipas.app.resep_makanan.repository;

import bcc.sipas.entity.ResepMakanan;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ResepMakananRepository {

    @Autowired
    private IResepMakananRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<ResepMakanan> create(ResepMakanan resepMakanan){
        return this.repository
                .save(resepMakanan);
    }

    public Mono<Page<ResepMakanan>> getList(Long id, Pageable pageable){
        Query query = Query.query(
                CriteriaDefinition.from(
                        Criteria.where("fk_faskes_id").is(id)
                                .and(
                                        Criteria.where("deleted_at").isNull()
                                )
                )
        ).limit(pageable.getPageSize()).offset(pageable.getOffset());
        return this.template
                .select(query, ResepMakanan.class)
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data resep makanan tidak ditemukan")))
                .collectList()
                .zipWith(this.repository.count())
                .flatMap((d) -> Mono.fromCallable(() -> new PageImpl<>(d.getT1(), pageable, d.getT2())));
    }

    public Mono<Page<ResepMakanan>> getList(Long id, ResepMakanan resepMakanan, Pageable pageable){
        Optional<Query> optQuery = QueryUtils.createQuerySearch(resepMakanan, false);
        Query query = optQuery.orElse(Query.empty());
        query = query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        return this.template
                .select(query, ResepMakanan.class)
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data resep makanan tidak ditemukan")))
                .collectList()
                .zipWith(this.repository.count())
                .map((t) -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }

    public Mono<Void> delete(Long id){
        return this.repository
                .findOne(Example.of(ResepMakanan.builder().id(id).build()))
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data resep makanan tidak ditemukan")))
                .flatMap((e) -> {
                    e.setDeletedAt(LocalDate.now());
                    return this.repository.save(e);
                })
                .then(Mono.empty());
    }

    public Mono<Long> count(Example<ResepMakanan> example){
        return this.repository
                .count(example);
    }
}
