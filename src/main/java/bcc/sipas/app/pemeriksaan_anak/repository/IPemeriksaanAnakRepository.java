package bcc.sipas.app.pemeriksaan_anak.repository;

import bcc.sipas.entity.DataPemeriksaanAnak;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPemeriksaanAnakRepository extends R2dbcRepository<DataPemeriksaanAnak, Long> {}
