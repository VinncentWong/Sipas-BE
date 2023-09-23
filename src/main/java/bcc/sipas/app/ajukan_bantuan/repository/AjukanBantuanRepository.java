package bcc.sipas.app.ajukan_bantuan.repository;

import bcc.sipas.app.orang_tua_faskes.repository.OrangtuaFaskesRepository;
import bcc.sipas.constant.AjukanBantuanRedisConstant;
import bcc.sipas.constant.OrangtuaFaskesRedisConstant;
import bcc.sipas.entity.AjukanBantuan;
import bcc.sipas.entity.OrangtuaFaskes;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.mapper.AjukanBantuanMapper;
import bcc.sipas.util.ObjectMapperUtils;
import bcc.sipas.util.PageableUtils;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AjukanBantuanRepository {

    @Autowired
    private IAjukanBantuanRepository repository;

    @Autowired
    private OrangtuaFaskesRepository ortuFaskesRepository;

    @Autowired
    private R2dbcEntityTemplate template;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<AjukanBantuan> create(AjukanBantuan ajukanBantuan){
        return this.repository.save(ajukanBantuan);
    }

    public Mono<Page<AjukanBantuan>> getListFaskes(Long faskesId, String statusAjuan, Pageable pageable){
        var ops = this.redisTemplate.opsForValue();
        var pageableStr = PageableUtils.toString(pageable);
        return this.ortuFaskesRepository
                .getList(faskesId, Pageable.unpaged())
                .map(Page::getContent)
                .map(v -> v.stream().parallel().map(OrangtuaFaskes::getFkOrtuId).toList())
                .flatMap((fkOrtuIds) -> {
                    var key = String.format(AjukanBantuanRedisConstant.GET_LIST_FASKES, faskesId, fkOrtuIds, statusAjuan, pageableStr);
                    return ops
                            .get(key)
                            .switchIfEmpty(Mono.defer(() -> {
                                log.info("redis result null on AjukanBantuanRepository.getListFaskes");
                                Query query = Query.query(
                                        Criteria.where("fk_ortu_id").in(fkOrtuIds)
                                                .and(
                                                        Criteria.where("deleted_at").isNull()
                                                )
                                                .and(
                                                        Criteria.where("status").is(statusAjuan)
                                                )
                                ).with(pageable);
                                return this.template
                                        .select(query, AjukanBantuan.class)
                                        .collectList()
                                        .map(ObjectMapperUtils::writeValueAsString);
                            }))
                            .flatMap((str) -> {
                                var ajukanBantuan = ObjectMapperUtils.readListValue(str, AjukanBantuan.class);
                                return ops
                                        .set(key, str, Duration.ofMinutes(1))
                                        .then(Mono.just(ajukanBantuan));
                            });
                })
                .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data ajukan bantuan tidak ditemukan")))
                .zipWith(this.repository.count())
                .map((list) -> new PageImpl<>(list.getT1(), pageable, list.getT2()));
    }

    public Mono<Page<AjukanBantuan>> getListOrtu(Long ortuId, Pageable pageable){
        var ops = this.redisTemplate.opsForValue();
        var key = String.format(AjukanBantuanRedisConstant.GET_LIST_ORTU, ortuId, PageableUtils.toString(pageable));
        return ops.get(key)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis result null on AjukanBantuanRepository.getListOrtu");
                    Query query = Query.query(
                            Criteria.where("fk_ortu_id").is(ortuId)
                                    .and(
                                            Criteria.where("deleted_at").isNull()
                                    )
                    ).with(pageable);
                    return this.template
                            .select(query, AjukanBantuan.class)
                            .collectList()
                            .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data ajukan bantuan tidak ditemukan")))
                            .zipWith(this.repository.count())
                            .map((l) -> new PageImpl<>(l.getT1(), pageable, l.getT2()))
                            .map(ObjectMapperUtils::writeValueAsString);
                }))
                .flatMap((str) -> {
                    var page = ObjectMapperUtils.readPageValue(str, AjukanBantuan.class);
                    return ops.set(key, str, Duration.ofMinutes(1))
                            .then(Mono.just(page));
                });
    }

    public Mono<AjukanBantuan> getById(Long id){
        var ops = this.redisTemplate.opsForValue();
        var key = String.format(AjukanBantuanRedisConstant.GET_BY_ID, id);
        ops.get(key)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis result null on AjukanBantuanRepository.getById");
                    return this.repository
                            .findById(id)
                            .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data ajukan bantuan tidak ditemukan")))
                            .map(ObjectMapperUtils::writeValueAsString);
                }))
                .flatMap((str) -> {
                    var ajukanBantuan = ObjectMapperUtils.readValue(str, AjukanBantuan.class);
                    return ops.set(key, str, Duration.ofMinutes(1))
                            .then(Mono.just(ajukanBantuan));
                });
    }

    public Mono<AjukanBantuan> update(Long id, AjukanBantuan ajukanBantuan){
        return this.repository
                .findById(id)
                .map((v) -> AjukanBantuanMapper.INSTANCE.updateAjukanBantuan(ajukanBantuan, v))
                .flatMap((v) -> this.repository.save(v));
    }

    public Mono<List<AjukanBantuan>> count(Long faskesId){
        return this.ortuFaskesRepository
                .getList(faskesId, Pageable.unpaged())
                .map(Page::getContent)
                .map((res) -> res.stream().parallel().map(OrangtuaFaskes::getFkOrtuId).toList())
                .flatMap((ortuIds) -> {
                    Query query = Query.query(
                            Criteria.where("fk_ortu_id").in(ortuIds)
                                    .and(
                                            Criteria.where("deleted_at").isNull()
                                    )
                    );
                    return this.template
                            .select(query, AjukanBantuan.class)
                            .collectList();
                });
    }
}
