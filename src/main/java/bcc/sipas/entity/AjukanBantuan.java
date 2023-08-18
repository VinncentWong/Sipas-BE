package bcc.sipas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
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

    @Id
    private Long id;

    private String judul;

    private String deskripsi;

    private StatusAjuan status;

    private String pesanTambahan;

    @Column("fk_ortu_id")
    private Integer fkOrtuId;

    @Column("fk_faskes_id")
    private Integer fkFaskesId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate deletedAt;
}
