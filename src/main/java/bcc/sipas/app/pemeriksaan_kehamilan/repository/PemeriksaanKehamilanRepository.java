package bcc.sipas.app.pemeriksaan_kehamilan.repository;

import bcc.sipas.entity.DataPemeriksaanKehamilan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PemeriksaanKehamilanRepository {

    @Autowired
    private IPemeriksaanKehamilanRepository repository;

    public Mono<DataPemeriksaanKehamilan> save(Long ortuId, Long faskesId, DataPemeriksaanKehamilan data){
        data.setFkOrtuId(ortuId);
        data.setFkFaskesId(faskesId);
        return this.repository.save(data);
    }
}
