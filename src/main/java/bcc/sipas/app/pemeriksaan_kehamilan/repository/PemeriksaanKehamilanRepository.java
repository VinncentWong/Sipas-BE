package bcc.sipas.app.pemeriksaan_kehamilan.repository;

import bcc.sipas.entity.DataPemeriksaanKehamilan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PemeriksaanKehamilanRepository {

    @Autowired
    private IPemeriksaanKehamilanRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<DataPemeriksaanKehamilan> save(Long ortuId, Long faskesId, Long dataKehamilanId, DataPemeriksaanKehamilan data){
        data.setFkOrtuId(ortuId);
        data.setFkFaskesId(faskesId);
        data.setFkDataKehamilan(dataKehamilanId);
        return this.repository.save(data);
    }

    public Mono<Page<DataPemeriksaanKehamilan>> getList(Long ortuId, Long faskesId, Long dataKehamilanId, Pageable page){
        Query query = Query.query(
                CriteriaDefinition.from(
                        Criteria.where("fk_ortu_id").is(ortuId)
                                .and(
                                        Criteria.where("fk_faskes_id").is(faskesId)
                                )
                                .and(
                                        Criteria.where("fk_data_kehamilan").is(dataKehamilanId)
                                )
                )
        )
        .limit(page.getPageSize())
        .offset(page.getOffset());
        return  this.template
                .select(query, DataPemeriksaanKehamilan.class)
                .collectList()
                .zipWith(this.repository.count())
                .flatMap((t) -> Mono.<Page<DataPemeriksaanKehamilan>>fromCallable(() -> new PageImpl<>(t.getT1(), page, t.getT2())));
    }

    public Mono<List<DataPemeriksaanKehamilan>> count(List<Long> ids){
        Query query = Query.query(
                Criteria.where("fk_ortu_id").in(ids)
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        );
        return this.template
                .select(query, DataPemeriksaanKehamilan.class)
                .collectList();
    }
}
