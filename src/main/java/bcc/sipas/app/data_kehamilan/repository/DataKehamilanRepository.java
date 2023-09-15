package bcc.sipas.app.data_kehamilan.repository;

import bcc.sipas.entity.DataKehamilan;
import bcc.sipas.exception.DataTidakDitemukanException;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DataKehamilanRepository {

    @Autowired
    private IDataKehamilanRepository dataKehamilanRepository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<DataKehamilan> save(DataKehamilan data){
        return this.dataKehamilanRepository.save(data);
    }

    public  Mono<DataKehamilan> find(Example<DataKehamilan> example){
        return this.dataKehamilanRepository.findOne(example).switchIfEmpty(Mono.error(new DataTidakDitemukanException("data kehamilan tidak ditemukan")));
    }

    public Mono<Long> count(Example<DataKehamilan> example){
        return this.dataKehamilanRepository.count(example);
    }

    public Mono<List<DataKehamilan>> count(List<Long> ids, Long fkFaskesId){
        Query query = Query.query(
                CriteriaDefinition.from(
                        Criteria.where("fk_ortu_id").in(ids)
                                .and(
                                        Criteria.where("deleted_at").isNull()
                                )
                                .and(
                                        Criteria.where("fk_faskes_id").is(fkFaskesId)
                                )
                )
        );
        return this.template
                .select(query, DataKehamilan.class)
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data kehamilan tidak ditemukan")))
                .collectList();
    }
}
