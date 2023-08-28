package bcc.sipas.app.whatsapp.repository;

import bcc.sipas.entity.GrupWhatsapp;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IWhatsappRepository extends R2dbcRepository<GrupWhatsapp, Long> { }
