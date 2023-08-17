package bcc.sipas.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("ajukan_bantuan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AjukanBantuan {

    private Long id;

    private String judul;

    private String deskripsi;

    private StatusAjuan status;

    private String pesanTambahan;

    @Column("fk_ortu_id")
    private Integer fkOrtuId;

    @Column("fk_faskes_id")
    private Integer fkFaskesId;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;
}
