package bcc.sipas.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("orangtua_faskes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrangtuaFaskes {

    private Long id;

    @Column("fk_ortu_id")
    private Integer fkOrtuId;

    @Column("fk_faskes_id")
    private Integer fkFaskesId;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;
}
