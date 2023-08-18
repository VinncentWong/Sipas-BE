package bcc.sipas.app.ortu.repository;

import bcc.sipas.entity.Orangtua;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface IOrangtuaRepository extends R2dbcRepository<Orangtua, Long> {

    @Query("SELECT * FROM \"orang_tua\" WHERE email = $1")
    Mono<Orangtua> findByEmail(String email);
}
