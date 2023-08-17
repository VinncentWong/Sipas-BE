package bcc.sipas.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("data_pemeriksaan_kehamilan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DataPemeriksaanKehamilan {

    private Long id;

    private LocalDate tanggalPemeriksaan;

    private String tempatPemeriksaan;

    private String namaPemeriksa;

    private Integer usiaKandungan;

    private String tekananDarah;

    private Integer beratBadanIbu;

    private StatusKehamilan statusKehamilan;

    private String pesanTambahan;

    @Column("fk_ortu_id")
    private Integer fkOrtuId;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @Column("fk_faskes_id")
    private Integer fkFaskesId;
}
