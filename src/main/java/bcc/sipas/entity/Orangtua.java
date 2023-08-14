package bcc.sipas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table(name = "orang_tua")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Orangtua {

    private Long id;

    private String namaIbu;

    private String namaAyah;

    private String email;

    @JsonIgnore
    private String password;

    private boolean isConnectedWithFaskes;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

    private List<DataAnak> dataAnak;

    private List<DataKehamilan> dataKehamilan;
}
