package bcc.sipas.app.ortu.repository;

import bcc.sipas.entity.Orangtua;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class OrangtuaRepository {

    @Autowired
    private PostgresqlConnectionFactory factory;

    @Autowired
    private IOrangtuaRepository repository;

    public Mono<Orangtua> save(Orangtua orangtua){
        orangtua.setCreatedAt(LocalDate.now());
        return Mono.from(factory
                .create()
                .flatMapMany((c) -> c.createStatement(IOrangtuaRepository.createSql)
                        .bind("$1", orangtua.getNamaIbu())
                        .bind("$2", orangtua.getNamaAyah())
                        .bind("$3", orangtua.getEmail())
                        .bind("$4", orangtua.getPassword())
                        .bind("$5", orangtua.isConnectedWithFaskes())
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
        return this.repository.findByEmail(email);
    }
}
