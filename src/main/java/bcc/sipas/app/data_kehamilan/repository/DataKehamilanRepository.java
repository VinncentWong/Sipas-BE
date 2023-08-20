package bcc.sipas.app.data_kehamilan.repository;

import bcc.sipas.entity.DataKehamilan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DataKehamilanRepository {

    @Autowired
    private IDataKehamilanRepository dataKehamilanRepository;

    public Mono<DataKehamilan> save(DataKehamilan data){
        return this.dataKehamilanRepository.save(data);
    }

    public  Mono<DataKehamilan> find(Example<DataKehamilan> example){
        return this.dataKehamilanRepository.findOne(example);
    }
}
