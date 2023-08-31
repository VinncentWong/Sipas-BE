package bcc.sipas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("resep_makanan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ResepMakanan {

    @Id
    private Long id;

    private String publicId;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate deletedAt;

    @Column("fk_faskes_id")
    private Long fkFaskesId;
}
