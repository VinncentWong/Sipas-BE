package bcc.sipas.app.artikel.repository;

import bcc.sipas.entity.Artikel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IArtikelRepository extends R2dbcRepository<Artikel, Long> {}
