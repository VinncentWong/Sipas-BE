package bcc.sipas.app.ortu.repository;

import bcc.sipas.entity.Orangtua;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrangtuaRepository extends ReactiveCrudRepository<Orangtua, Long> { }
