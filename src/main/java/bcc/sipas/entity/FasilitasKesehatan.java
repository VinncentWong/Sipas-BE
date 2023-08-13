package bcc.sipas.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
public class FasilitasKesehatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String username;

    private String kodeUnik;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    private LocalDate deletedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faskes", fetch = FetchType.LAZY)
    private List<DataPemeriksaanKehamilan> dataPemeriksaanKehamilan;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faskes", fetch = FetchType.LAZY)
    private List<GrupWhatsapp> grupWhatsapp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faskes", fetch = FetchType.LAZY)
    private List<ResepMakanan> resepMakanan;
}
