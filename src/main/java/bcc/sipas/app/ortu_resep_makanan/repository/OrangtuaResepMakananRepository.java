package bcc.sipas.app.ortu_resep_makanan.repository;

import bcc.sipas.entity.OrangtuaResepMakanan;
import bcc.sipas.exception.DataTidakDitemukanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class OrangtuaResepMakananRepository {

    @Autowired
    private IOrangtuaResepMakananRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<OrangtuaResepMakanan> create(Long ortuId, Long resepMakananId){
        OrangtuaResepMakanan orangtuaResepMakanan = OrangtuaResepMakanan
                .builder()
                .fkOrtuId(ortuId)
                .fkResepMakananId(resepMakananId)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        return this.repository.save(orangtuaResepMakanan);
    }

    public Mono<OrangtuaResepMakanan> get(Long ortuId, Long resepMakananId){
        Query query = Query.query(
                Criteria.where("fk_ortu_id").is(ortuId)
                        .and(
                                Criteria.where("fk_resep_makanan_id").is(resepMakananId)
                        )
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        );
        return this.template
                .select(query, OrangtuaResepMakanan.class)
                .collectList()
                .flatMap((list) -> {
                    if(!list.isEmpty()){
                        return Mono.just(list.get(0));
                    } else {
                        return Mono.error(new DataTidakDitemukanException("data orangtua resep makanan tidak ditemukan"));
                    }
                });
    }

    public Mono<OrangtuaResepMakanan> update(Long ortuResepMakananId, boolean remove){
        return this.repository
                .findById(ortuResepMakananId)
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua resep makanan tidak ditemukan")))
                .map((v) -> {
                    if(remove){
                        v.setDeletedAt(LocalDate.now());
                    } else {
                        v.setDeletedAt(null);
                        v.setUpdatedAt(LocalDate.now());
                    }
                    return v;
                })
                .flatMap((v) -> this.repository.save(v));
    }
}
