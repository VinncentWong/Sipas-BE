package bcc.sipas.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Table("data_kehamilan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DataKehamilan {

    private Long id;

    private JenisKelamin namaCalonBayi;

    private LocalDate tanggalPertamaHaid;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @Column("fk_ortu_id")
    private Orangtua orangtua;
}
