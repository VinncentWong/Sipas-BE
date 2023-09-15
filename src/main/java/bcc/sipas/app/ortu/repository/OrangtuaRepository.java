package bcc.sipas.app.ortu.repository;

import bcc.sipas.entity.Orangtua;
import bcc.sipas.exception.DataTidakDitemukanException;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
public class OrangtuaRepository {

    @Autowired
    private PostgresqlConnectionFactory factory;

    @Autowired
    private IOrangtuaRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<Orangtua> save(Orangtua orangtua){
        orangtua.setCreatedAt(LocalDate.now());
        orangtua.setIsConnectedWithFaskes(false);
        return Mono.from(factory
                .create()
                .flatMapMany((c) -> c.createStatement(IOrangtuaRepository.createSql)
                        .bind("$1", orangtua.getNamaIbu())
                        .bind("$2", orangtua.getNamaAyah())
                        .bind("$3", orangtua.getEmail())
                        .bind("$4", orangtua.getPassword())
                        .bind("$5", orangtua.getIsConnectedWithFaskes())
                        .bind("$6", orangtua.getCreatedAt())
                        .returnGeneratedValues("id")
                        .execute())
                .flatMap((res) -> Mono.from(res.map((row, metadata) -> (Long)row.get("id"))))
                .flatMap((res) -> {
                    orangtua.setId(res);
                    return Mono.just(orangtua);
                })
        );
    }

    public Mono<Orangtua> findByEmail(String email){
        return this.repository.findByEmail(email).switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")));
    }

    public Mono<Orangtua> findOne(Example<Orangtua> example){
        return this.repository.findOne(example).switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")));
    }

    public Mono<Orangtua> findById(Long id){
        return this.repository.findById(id).switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")));
    }

    public Mono<List<Orangtua>> findAll(List<Long> ids, String namaOrtu){
        Query query = Query.query(
                Criteria.where("id").in(ids)
                        .and(
                                Criteria.where("nama_ayah").like(String.format("%%%s%%", namaOrtu)).ignoreCase(true)
                                        .or(
                                                Criteria.where("nama_ibu").like(String.format("%%%s%%", namaOrtu)).ignoreCase(true)
                                        )
                        )
                        .and(
                                Criteria.where("deleted_at").isNull()
                        )
        );
        return this.template
                .select(query, Orangtua.class)
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")))
                .collectList();
    }

    public Mono<Orangtua> update(Orangtua orangtua){
        return this.repository
                .save(orangtua);
    }
}
