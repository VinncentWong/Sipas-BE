package bcc.sipas.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DataAnak {

    private Long id;

    private String namaAnak;

    private String jenisKelamin;

    private LocalDate tanggalLahir;

    private KondisiLahir kondisiLahir;

    private Integer beratBadanLahir;

    private Integer panjangBadanLahir;

    private Integer lingkarKepala;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @Column("fk_ortu_id")
    private Long fkOrtuId;
}
