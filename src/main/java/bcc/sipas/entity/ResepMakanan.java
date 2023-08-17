package bcc.sipas.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("resep_makanan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ResepMakanan {

    private String id;

    private String urlGambar;

    private String judulResep;

    private String targetResep;

    private String targetUsiaResep;

    private String jenis;

    private String bahanUtama;

    private String durasiMemasak;

    private String bahanText;

    private String caraMembuatText;

    private String nilaiGiziText;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @Column("fk_faskes_id")
    private Long fkFaskesId;
}
