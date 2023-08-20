package bcc.sipas.app.orang_tua_faskes.repository;

import bcc.sipas.entity.FasilitasKesehatan;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.OrangtuaFaskes;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class OrangtuaFaskesRepository {

    @Autowired
    private PostgresqlConnectionFactory factory;

    @Autowired
    private IOrangtuaFaskesRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<OrangtuaFaskes> create(OrangtuaFaskes ortuFaskes){
        return this.template
                .insert(OrangtuaFaskes.class)
                .using(ortuFaskes);
    }

    public Flux<Page<OrangtuaFaskes>> getList(Long faskesId, Pageable pageable){
        Query query = Query.query(
                CriteriaDefinition.from(
                        Criteria.where("fk_faskes_id").is(faskesId)
                )
        ).with(pageable);
        return Flux.from(this.template.select(query, OrangtuaFaskes.class))
                .collectList()
                .zipWith(this.repository.count())
                .flatMapMany((t) -> Mono.fromCallable(() -> new PageImpl<>(t.getT1(), pageable, t.getT2())));
    }
}