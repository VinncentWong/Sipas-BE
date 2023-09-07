package bcc.sipas.app.artikel.repository;

import bcc.sipas.entity.Artikel;
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

@Component
public class ArtikelRepository {

    @Autowired
    private IArtikelRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<Artikel> save(Artikel artikel){
        return this.repository.save(artikel);
    }

    public Mono<Page<Artikel>> getList(Long faskesId, Pageable page){
        Query query = Query
                .query(
                        CriteriaDefinition.from(
                                Criteria.where("fk_faskes_id").is(faskesId)
                                        .and(
                                                Criteria.where("deleted_at").isNull()
                                        )
                        )
                )
                .offset(page.getOffset())
                .limit(page.getPageSize());
        return this
                .template
                .select(query, Artikel.class)
                .switchIfEmpty(Mono.just(Artikel.builder().build()))
                .collectList()
                .zipWith(this.repository.count())
                .flatMap((d) -> Mono.fromCallable(() -> new PageImpl<>(d.getT1(), page, d.getT2())));
    }

    public Mono<Void> delete(Long id){
        return this.repository
                .findById(id)
                .flatMap((d) -> {
                    d.setDeletedAt(LocalDate.now());
                    return this.repository.save(d);
                })
                .then(Mono.empty());
    }

    public Mono<Long> count(Example<Artikel> example){
        return this.repository
                .count(example);
    }
}
