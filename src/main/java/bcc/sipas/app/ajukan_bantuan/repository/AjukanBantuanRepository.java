package bcc.sipas.app.ajukan_bantuan.repository;

import bcc.sipas.entity.AjukanBantuan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AjukanBantuanRepository {

    @Autowired
    private IAjukanBantuanRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<AjukanBantuan> create(AjukanBantuan ajukanBantuan){
        return this.repository.save(ajukanBantuan);
    }
}
