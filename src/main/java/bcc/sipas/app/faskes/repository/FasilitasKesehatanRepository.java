package bcc.sipas.app.faskes.repository;

import bcc.sipas.entity.FasilitasKesehatan;
import bcc.sipas.entity.OrangtuaFaskes;
import bcc.sipas.exception.DataTidakDitemukanException;
import bcc.sipas.exception.DatabaseException;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public final class FasilitasKesehatanRepository {

    @Autowired
    private PostgresqlConnectionFactory factory;

    @Autowired
    private IFasilitasKesehatanRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<FasilitasKesehatan> save(FasilitasKesehatan faskes){
        return this.template
                .insert(FasilitasKesehatan.class)
                .using(faskes)
                .switchIfEmpty(Mono.error(new DatabaseException("terjadi kesalahan saat menyimpan data fasilitas kesehatan")));
    }

    public Mono<FasilitasKesehatan> findByEmail(String email){
        return this.repository.findByEmail(email).switchIfEmpty(Mono.error(new DataTidakDitemukanException("data faskes tidak ditemukan")));
    }

    public Mono<FasilitasKesehatan> findOne(Example<FasilitasKesehatan> example){
        return this.repository.findOne(example).switchIfEmpty(Mono.error(new DataTidakDitemukanException("data faskes tidak ditemukan")));
    }
}
