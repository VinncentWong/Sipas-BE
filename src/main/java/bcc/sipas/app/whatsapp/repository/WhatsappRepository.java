package bcc.sipas.app.whatsapp.repository;

import bcc.sipas.entity.GrupWhatsapp;
import bcc.sipas.exception.DataTidakDitemukanException;
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

@Component
@Slf4j
public class WhatsappRepository {

    @Autowired
    private IWhatsappRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<GrupWhatsapp> save(GrupWhatsapp data){
        return this
                .repository
                .save(data);
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

    public Mono<Page<GrupWhatsapp>> getList(Long faskesId, Pageable page){
        Query query = Query.query(
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
                .select(query, GrupWhatsapp.class)
                .collectList()
                .zipWith(this.repository.count())
                .flatMap((t) -> Mono.<Page<GrupWhatsapp>>fromCallable(() -> new PageImpl<>(t.getT1(), page, t.getT2())));
    }

    public Mono<GrupWhatsapp> get(Example<GrupWhatsapp> ex){
        return this.repository
                .findOne(ex);
    }
}
