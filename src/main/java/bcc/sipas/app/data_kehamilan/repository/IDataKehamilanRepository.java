package bcc.sipas.app.data_kehamilan.repository;

import bcc.sipas.entity.DataKehamilan;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IDataKehamilanRepository extends R2dbcRepository<DataKehamilan, Long>{}
