package bcc.sipas.app.resep_makanan.repository;

import bcc.sipas.entity.ResepMakanan;
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

@Component
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
                .collectList()
                .zipWith(this.repository.count())
                .flatMap((d) -> Mono.fromCallable(() -> new PageImpl<>(d.getT1(), pageable, d.getT2())));
    }

    public Mono<Void> delete(Long id){
        return this.repository
                .findOne(Example.of(ResepMakanan.builder().id(id).build()))
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