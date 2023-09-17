package bcc.sipas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("orangtua_resep_makanan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrangtuaResepMakanan {

    @Id
    private Long id;

    @Column("fk_ortu_id")
    private Long fkOrtuId;

    @Column("fk_resep_makanan_id")
    private Long fkResepMakananId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate deletedAt;
}
