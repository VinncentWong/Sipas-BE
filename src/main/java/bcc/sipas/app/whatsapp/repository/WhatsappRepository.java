package bcc.sipas.app.whatsapp.repository;

import bcc.sipas.app.orang_tua_faskes.repository.OrangtuaFaskesRepository;
import bcc.sipas.entity.GrupWhatsapp;
import bcc.sipas.entity.OrangtuaFaskes;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.exception.DatabaseException;
import bcc.sipas.util.ResponseUtil;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class WhatsappRepository {

    @Autowired
    private IWhatsappRepository repository;

    @Autowired
    private OrangtuaFaskesRepository ortuFaskesRepository;

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
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data whatsapp tidak ditemukan")))
                .collectList()
                .zipWith(this.repository.count())
                .flatMap((t) -> Mono.<Page<GrupWhatsapp>>fromCallable(() -> new PageImpl<>(t.getT1(), page, t.getT2())));
    }

    public Mono<GrupWhatsapp> get(Example<GrupWhatsapp> ex){
        return this.repository
                .findOne(ex);
    }

    public Mono<Page<GrupWhatsapp>> getListForOrtu(Long ortuId, Pageable pageable){
        return this.ortuFaskesRepository
                .getListForOrtu(ortuId, Pageable.unpaged())
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data whatsapp tidak ditemukan")))
                .map(Page::getContent)
                .map((ortuFaskes) -> ortuFaskes.stream().map(OrangtuaFaskes::getFkFaskesId).toList())
                .map((faskesIds) ->
                     faskesIds.stream().parallel().map((id) -> {
                         Query query = Query.query(
                                 Criteria.where("fk_faskes_id").is(id)
                                         .and(
                                                 Criteria.where("deleted_at").isNull()
                                         )
                         );
                         return this.template.select(query, GrupWhatsapp.class);
                     }
                    ).toList()
                )
                .map((listFluxs) -> listFluxs.stream().reduce((Flux::concat)))
                .map((d) -> d.orElse(Flux.error(new DatabaseException("data whatsapp tidak ditemukan"))))
                .flatMap((d) -> d.collectList())
                .zipWith(this.repository.count())
                .map((grupWhatsapps -> new PageImpl<>(grupWhatsapps.getT1(), pageable, grupWhatsapps.getT2())));
    }
}
