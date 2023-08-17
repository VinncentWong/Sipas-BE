package bcc.sipas.entity;


import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table("fasilitas_kesehatan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FasilitasKesehatan {

    private Long id;

    private String email;

    private String password;

    private String username;

    private String kodeUnik;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;
}
