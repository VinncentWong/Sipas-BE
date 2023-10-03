package bcc.sipas.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table("fasilitas_kesehatan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class FasilitasKesehatan {

    @Id
    private Long id;

    private String email;

    private String password;

    private String username;

    private String kodeUnik;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate deletedAt;

    private String nomorTelepon;

    private String alamatFaskes;
}
