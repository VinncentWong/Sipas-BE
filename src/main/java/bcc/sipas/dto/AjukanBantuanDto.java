package bcc.sipas.dto;

import bcc.sipas.entity.AjukanBantuan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AjukanBantuanDto {

    public record Create(
            @NotNull(message = "judul pengajuan harus ada")
            @NotBlank(message = "judul pengajuan tidak boleh kosong")
            String judulPengajuan,

            @NotNull(message = "deskripsi pengajuan harus ada")
            @NotBlank(message = "deskripsi pengajuan tidak boleh kosong")
            String deskripsiPengajuan
    ){
        public AjukanBantuan toAjukanBantuan(){
            return AjukanBantuan
                    .builder()
                    .judul(this.judulPengajuan)
                    .deskripsi(this.deskripsiPengajuan)
                    .build();
        }
    }
}
