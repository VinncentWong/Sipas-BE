package bcc.sipas.dto;

import bcc.sipas.entity.Orangtua;
import bcc.sipas.util.BcryptUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class OrangtuaDto {

    public record Create(
            @NotNull(message = "nama ibu harus ada")
            @NotBlank(message = "nama ibu tidak boleh kosong")
            String namaIbu,

            @NotNull(message = "nama ayah harus ada")
            @NotBlank(message = "nama ayah tidak boleh kosong")
            String namaAyah,

            @Email
            @NotNull(message = "email harus ada")
            @NotBlank(message = "email tidak boleh kosong")
            String email,

            @NotNull(message = "password harus ada")
            @NotBlank(message = "password tidak boleh kosong")
            @Length(message = "panjang minimal 4", min = 4)
            String password
    ){
        public Orangtua toOrangtua(){
            return Orangtua.builder()
                    .namaAyah(this.namaAyah)
                    .namaIbu(this.namaIbu)
                    .email(this.email)
                    .password(BcryptUtil.encode(this.password))
                    .build();
        }
    }
}
