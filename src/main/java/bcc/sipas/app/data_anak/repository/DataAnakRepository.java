package bcc.sipas.app.data_anak.repository;

import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.DataPemeriksaanAnak;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlConnection;
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
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@Slf4j
public class DataAnakRepository {

    @Autowired
    private IDataAnakRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<DataAnak> save(DataAnak dataAnak){
        return this.repository.save(dataAnak);
    }

    public Flux<DataAnak> getList(Example<DataAnak> example){
        return this.repository.findAll(example);
    }

    public Mono<Page<DataAnak>> getList(Long id, Pageable pageable){
        return this.template
                .select(DataAnak.class)
                .matching(
                        Query.query(
                                CriteriaDefinition.from(
                                        Criteria.where("fk_ortu_id").is(id)
                                )
                        )
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                ).all()
                .collectList()
                .zipWith(this.repository.count())
                .flatMap((d) -> {
                    List<DataAnak> listData = d.getT1();
                    Long n = d.getT2();
                    return Mono.fromCallable(() -> new PageImpl<>(listData, pageable, n.intValue()));
                });
    }

    public Mono<Page<DataAnak>> getList(List<Long> ortuIds, Pageable page){
        Query query = Query.query(
                Criteria.where("fk_ortu_id").in(ortuIds)
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        ).with(page);
        return this.template
                .select(query, DataAnak.class)
                .collectList()
                .zipWith(this.repository.count())
                .map((d) -> new PageImpl<>(d.getT1(), page, d.getT2()));
    }

    /**
     * This method get list data of Data Anak filtered by nama orangtua, and faskes id
     */
    public Mono<Page<DataAnak>> getList(String namaOrtu, List<Long> ortuIds, Pageable pageable){
        Query query = Query.query(
                Criteria.where("fk_ortu_id").in(ortuIds)
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        ).with(pageable);
        return this.template
                .select(query, DataAnak.class)
                .collectList()
                .zipWith(this.repository.count())
                .map((dataAnaks -> new PageImpl<>(dataAnaks.getT1(), pageable, dataAnaks.getT2())));
    }
}