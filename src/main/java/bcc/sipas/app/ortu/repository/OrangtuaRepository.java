package bcc.sipas.app.ortu.repository;

import bcc.sipas.constant.OrangtuaRedisConstant;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.util.ObjectMapperUtils;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class OrangtuaRepository {

    @Autowired
    private PostgresqlConnectionFactory factory;

    @Autowired
    private IOrangtuaRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<Orangtua> save(Orangtua orangtua){
        var ops = this.redisTemplate.opsForValue();
        orangtua.setCreatedAt(LocalDate.now());
        orangtua.setIsConnectedWithFaskes(false);
        return Mono.from(
                factory
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
                    return ops.delete(OrangtuaRedisConstant.ALL)
                            .then(Mono.just(orangtua));
                })
        );
    }

    public Mono<Orangtua> findByEmail(String email){
        var ops = this.redisTemplate.opsForValue();
        return ops.get(String.format(OrangtuaRedisConstant.FIND_BY_EMAIL, email))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis result null on OrangtuaRepository.findByEmail");
                    return this.repository.findByEmail(email).map(ObjectMapperUtils::writeValueAsString);
                }))
                .flatMap((v) -> {
                    var ortu = ObjectMapperUtils.readValue(v, Orangtua.class);
                    return ops
                            .set(String.format(OrangtuaRedisConstant.FIND_BY_EMAIL, ortu.getEmail()), v, Duration.ofMinutes(1))
                            .then(Mono.just(ortu));
                });
    }

    public Mono<Orangtua> findOne(Example<Orangtua> example){
        var ops = this.redisTemplate.opsForValue();
        return ops.get(String.format(OrangtuaRedisConstant.FIND_ONE, example))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis result null on OrangtuaRepository.findOne");
                    return this.repository
                            .findOne(example)
                            .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")))
                            .map(ObjectMapperUtils::writeValueAsString);
                }))
                .flatMap((ortuStr) -> {
                    var ortu = ObjectMapperUtils.readValue(ortuStr, Orangtua.class);
                    return ops.set(String.format(OrangtuaRedisConstant.FIND_ONE, example), ortuStr, Duration.ofMinutes(1))
                            .then(Mono.just(ortu));
                });
    }

    public Mono<Orangtua> findById(Long id){
        var ops = this.redisTemplate.opsForValue();
        return ops.get(String.format(OrangtuaRedisConstant.FIND_BY_ID, id))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis result null on OrangtuaRepository.findId");
                    return this.repository
                            .findById(id)
                            .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data orangtua tidak ditemukan")))
                            .map(ObjectMapperUtils::writeValueAsString);
                }))
                .flatMap((ortuStr) -> {
                    var ortu = ObjectMapperUtils.readValue(ortuStr, Orangtua.class);
                    return ops
                            .set(String.format(OrangtuaRedisConstant.FIND_BY_ID, id), ortuStr, Duration.ofMinutes(1))
                            .then(Mono.just(ortu));
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<List<Orangtua>> findAll(List<Long> ids, String namaOrtu){
        var ops = this.redisTemplate.opsForValue();
        return ops.get(String.format(OrangtuaRedisConstant.GET_LIST_IDS, ids))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis result null on OrangtuaRepository.findAll");
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
                            .collectList()
                            .map(ObjectMapperUtils::writeValueAsString);
                }))
                .flatMap((listStr) -> {
                    var listOrtu = (List<Orangtua>)ObjectMapperUtils.readValue(listStr, List.class);
                    return ops
                            .set(String.format(OrangtuaRedisConstant.GET_LIST_IDS, ids), listStr, Duration.ofMinutes(1))
                            .then(Mono.just(listOrtu));
                });
    }

    public Mono<Orangtua> update(Orangtua orangtua){
        var ops = this.redisTemplate.opsForValue();
        return this.repository
                .save(orangtua)
                .flatMap((res) -> ops.delete(OrangtuaRedisConstant.ALL).then(Mono.just(res)));
    }
}
