package bcc.sipas.app.faskes.repository;

import bcc.sipas.constant.FaskesRedisConstant;
import bcc.sipas.entity.FasilitasKesehatan;
import bcc.sipas.entity.OrangtuaFaskes;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.exception.DatabaseException;
import bcc.sipas.util.ObjectMapperUtils;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;


@Component
@Slf4j
public final class FasilitasKesehatanRepository {

    @Autowired
    private PostgresqlConnectionFactory factory;

    @Autowired
    private IFasilitasKesehatanRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<FasilitasKesehatan> save(FasilitasKesehatan faskes){
        return this.template
                .insert(FasilitasKesehatan.class)
                .using(faskes)
                .switchIfEmpty(Mono.error(new DatabaseException("terjadi kesalahan saat menyimpan data fasilitas kesehatan")));
    }

    public Mono<FasilitasKesehatan> findByEmail(String email){
        var ops = this.redisTemplate.opsForValue();
        return ops.get(String.format(FaskesRedisConstant.FIND_BY_EMAIL, email))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis null on FasilitasKesehatanRepository.findByEmail");
                    return this.repository.findByEmail(email)
                            .map(ObjectMapperUtils::writeValueAsString);
                }))
                .log()
                .flatMap((faskesStr) -> {
                    var faskes = ObjectMapperUtils.readValue(faskesStr, FasilitasKesehatan.class);
                    return ops
                            .set(String.format(FaskesRedisConstant.FIND_BY_EMAIL, email), faskesStr, Duration.ofMinutes(1))
                            .then(Mono.just(faskes));
                });
    }

    public Mono<FasilitasKesehatan> findOne(Example<FasilitasKesehatan> example){
        var ops = this.redisTemplate.opsForValue();
        return ops.get(String.format(FaskesRedisConstant.FIND_ONE, example))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("redis null on FasilitasKesehatanRepository.findOne");
                    return
                            this.repository
                                    .findOne(example)
                                    .switchIfEmpty(Mono.error(new DataTidakDitemukanException("data faskes tidak ditemukan")))
                                    .map(ObjectMapperUtils::writeValueAsString);
                }))
                .flatMap((str) -> {
                    var faskes = ObjectMapperUtils.readValue(str, FasilitasKesehatan.class);
                    return ops
                            .set(String.format(FaskesRedisConstant.FIND_ONE, example), str, Duration.ofMinutes(1))
                            .then(Mono.just(faskes));
                });
    }
}
