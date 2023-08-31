package bcc.sipas.app.resep_makanan.repository;

import bcc.sipas.entity.ResepMakanan;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface IResepMakananRepository extends R2dbcRepository<ResepMakanan, Long> {}
