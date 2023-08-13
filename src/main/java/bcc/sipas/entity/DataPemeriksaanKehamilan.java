package bcc.sipas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
public class DataPemeriksaanKehamilan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate tanggalPemeriksaan;

    private String tempatPemeriksaan;

    private String namaPemeriksa;

    private Integer usiaKandungan;

    private String tekananDarah;

    private Integer beratBadanIbu;

    private StatusKehamilan statusKehamilan;

    private String pesanTambahan;

    @Column(name = "fk_ortu_id")
    private Integer fkOrtuId;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    private FasilitasKesehatan faskes;
}
