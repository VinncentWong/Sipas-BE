package bcc.sipas.app.data_anak.repository;

import bcc.sipas.entity.DataAnak;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DataAnakRepository {

    @Autowired
    private IDataAnakRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<DataAnak> save(DataAnak dataAnak){
        return this.repository.save(dataAnak);
    }

    public Mono<DataAnak> get(Example<DataAnak> example){
        return this.repository.findOne(example);
    }
}