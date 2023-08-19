package bcc.sipas.app.data_anak.repository;

import bcc.sipas.entity.DataAnak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
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
}