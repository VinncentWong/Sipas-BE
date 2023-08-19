package bcc.sipas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("orang_tua_faskes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrangtuaFaskes {

    @Id
    private Long id;

    @Column("fk_ortu_id")
    private Long fkOrtuId;

    @Column("fk_faskes_id")
    private Long fkFaskesId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate deletedAt;
}
