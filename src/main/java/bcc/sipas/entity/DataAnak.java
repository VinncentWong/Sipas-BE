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
public class DataAnak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaAnak;

    private String jenisKelamin;

    private LocalDate tanggalLahir;

    private KondisiLahir kondisiLahir;

    private Integer beratBadanLahir;

    private Integer panjangBadanLahir;

    private Integer lingkarKepala;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orangtua orangtua;
}
