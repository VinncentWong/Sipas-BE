package bcc.sipas.seeder;

import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import bcc.sipas.constant.SeedConstant;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class Seeder {

    @Autowired
    private OrangtuaRepository repository;

    @Qualifier("mysqlConnectionFactory")
    @Autowired
    private ConnectionFactory connectionFactory;

    /*
    somehow seed.sql can't work, will troubleshoot soon,
    however init.sql already work
     */
    @PostConstruct
    public void startInitalize(){
        if(this.connectionFactory instanceof PostgresqlConnectionFactory factory) {
            Mono<PostgresqlConnection> monoConnection = this.createPostgresqlConnection(factory);
            Flux<Integer> executeRes = monoConnection.flatMapMany((conn) -> {
                Mono<Void> initTable = Mono
                        .from(ScriptUtils.executeSqlScript(conn, new ClassPathResource(SeedConstant.initFile)))
                        .doOnError((e) -> {
                            log.error("error on executing {} with message {}", SeedConstant.initFile, e.getMessage());
                        })
                        .doOnSuccess((v) -> {
                            log.info("success executing {}", SeedConstant.initFile);
                        });
                Mono<PostgresqlResult> executeCount = Mono.from(
                        conn.createStatement("SELECT COUNT(*) FROM orang_tua").execute()
                        )
                        .doOnError((e) -> {
                            log.error("error when executing repository.count() with message {}", e.getMessage());
                        })
                        .doOnSuccess((v) -> {
                            log.info("success executing repository.count");
                        })
                        .doOnEach((s) -> conn.close());
                return Mono.zip(initTable, executeCount);
            }).flatMap((t) -> {
                PostgresqlResult res = t.getT2();
                List<Row> rows = new ArrayList<>();
                res.map((row, metadata) -> {
                    rows.add(row);
                    return row.toString();
                });
                log.info("rows.size = {}", rows.size());
                return Flux.just(rows.size());
            })
                    .doOnEach((s) -> {
                        log.info("flatMapMany = {}", s);
                    });
            Mono<Tuple3<Void, Void, Void>> finalRes = Mono.zip(monoConnection, Mono.from(executeRes))
                    .flatMap((t) -> {
                        PostgresqlConnection conn = t.getT1();
                        Integer n = t.getT2();
                        log.info("n = {}", n);
                        if(n == 0){
                            return Mono.zip(
                                    conn.beginTransaction(),
                                    Mono.from(ScriptUtils.executeSqlScript(conn, new ClassPathResource(SeedConstant.seedFile)))
                                            .doOnError((e) -> {
                                                conn.rollbackTransaction().block(Duration.ofSeconds(3L));
                                                log.error("error on executing {} with message {}", SeedConstant.seedFile, e.getMessage());
                                            })
                                            .doOnSuccess((v) -> {
                                                log.info("success executing {}", SeedConstant.seedFile);
                                            }),
                                    conn.commitTransaction()
                            ).doOnEach((s) -> conn.close());
                        } else {
                            return Mono.empty();
                        }
                    });

            finalRes
                    .subscribe(
                            (v) -> {
                                log.info("seeding success with rows updated {}", v);
                            },
                            (e) -> {
                                log.error("error occurred when seeding data with message {}", e.getMessage());
                            },
                            () -> {
                                log.info("seeding process complete");
                            }
                    );
        }
    }

    private Mono<PostgresqlConnection> createPostgresqlConnection(PostgresqlConnectionFactory factory){
        return Mono.from(factory.create())
                .doOnError((e) -> {
                    log.error("error occurred when create mysql connection with message {}", e.getMessage());
                })
                .doOnSuccess((v) -> {
                    log.info("success on create mysql connection");
                });
    }
}
