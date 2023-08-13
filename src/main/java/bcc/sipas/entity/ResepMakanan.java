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
public class ResepMakanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

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

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    private FasilitasKesehatan faskes;
}
