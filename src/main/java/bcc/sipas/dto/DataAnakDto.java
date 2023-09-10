package bcc.sipas.dto;

import bcc.sipas.constant.DateTimeConstant;
import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.JenisKelamin;
import bcc.sipas.entity.KondisiLahir;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataAnakDto {

    public record Create(
            @NotNull(message = "nama anak harus ada")
            @NotBlank(message = "nama anak tidak boleh kosong")
            String namaAnak,

            @NotNull(message = "tanggal lahir anak harus ada")
            @NotBlank(message = "tanggal lahir anak tidak boleh kosong")
            String tanggalLahirAnak,

            @Schema(name = "jenisKelamin",example = "laki/perempuan")
            @NotNull(message = "jenis kelamin harus ada")
            JenisKelamin jenisKelamin,

            @Schema(name = "kondisiLahir",example = "prematur/sehat/lainnya")
            @NotNull(message = "kondisi lahir harus ada")
            KondisiLahir kondisiLahir,

            @NotNull(message = "berat badan lahir harus ada")
            @PositiveOrZero(message = "nilai minimal adalah 0")
            Double beratBadanLahir,

            @NotNull(message = "panjang badan lahir harus ada")
            @PositiveOrZero(message = "nilai minimal adalah 0")
            Double panjangBadanLahir,

            @NotNull(message = "lingkar kepala harus ada")
            @PositiveOrZero(message = "nilai minimal adalah 0")
            Double lingkarKepala
    ){
            public DataAnak toDataAnak(){
                    return DataAnak
                            .builder()
                            .beratBadanLahir(this.beratBadanLahir)
                            .namaAnak(this.namaAnak)
                            .kondisiLahir(this.kondisiLahir.name())
                            .tanggalLahir(LocalDate.parse(this.tanggalLahirAnak, DateTimeFormatter.ofPattern(DateTimeConstant.DD_MM_YYYY)))
                            .jenisKelamin(this.jenisKelamin.name())
                            .lingkarKepala(this.lingkarKepala)
                            .panjangBadanLahir(this.panjangBadanLahir)
                            .createdAt(LocalDate.now())
                            .updatedAt(LocalDate.now())
                            .build();
            }
    }

        public record SearchByName(
                @NotNull(message = "nama ortu harus ada")
                @NotBlank(message = "nama ortu tidak boleh kosong")
                String namaOrtu
        ){}
}
