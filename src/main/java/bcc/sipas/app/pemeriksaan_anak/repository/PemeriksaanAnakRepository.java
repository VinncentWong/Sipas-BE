package bcc.sipas.app.pemeriksaan_anak.repository;

import bcc.sipas.entity.DataPemeriksaanAnak;
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

import java.util.List;

@Component
public class PemeriksaanAnakRepository {

    @Autowired
    private IPemeriksaanAnakRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<DataPemeriksaanAnak> save(DataPemeriksaanAnak data){
        return this.repository
                .save(data);
    }

    public Mono<Page<DataPemeriksaanAnak>> getList(Long ortuId, Long faskesId, Long kehamilanId, Pageable page){
        Query query = Query
                .query(
                        CriteriaDefinition.from(
                                Criteria.where("fk_ortu_id").is(ortuId)
                                        .and(
                                                Criteria.where("fk_faskes_id").is(faskesId)
                                        )
                                        .and(
                                                Criteria.where("fk_data_anak").is(kehamilanId)
                                        )
                        )
                )
                .offset(page.getOffset())
                .limit(page.getPageSize());
        return Mono.from(this
                .template
                .select(query, DataPemeriksaanAnak.class)
                .collectList()
                .zipWith(this.repository.count())
                .flatMapMany((t) -> Mono.fromCallable(() -> new PageImpl<>(t.getT1(), page, t.getT2())))
        );
    }
}
