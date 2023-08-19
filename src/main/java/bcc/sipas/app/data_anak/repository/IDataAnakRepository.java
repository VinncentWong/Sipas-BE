package bcc.sipas.app.data_anak.repository;

import bcc.sipas.entity.DataAnak;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IDataAnakRepository extends R2dbcRepository<DataAnak, Long> {}
