package bcc.sipas.app.ortu_resep_makanan.repository;

import bcc.sipas.entity.OrangtuaResepMakanan;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrangtuaResepMakananRepository extends R2dbcRepository<OrangtuaResepMakanan, Long> {}
