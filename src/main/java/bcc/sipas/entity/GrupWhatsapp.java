package bcc.sipas.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("grup_whatsapp")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class GrupWhatsapp {

    private Long id;

    private String namaGrup;

    private String linkGrup;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @Column("fk_faskes_id")
    private Long fkFaskesId;
}
