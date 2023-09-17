package bcc.sipas.app.ortu_resep_makanan.controller;

import bcc.sipas.app.ortu_resep_makanan.service.IOrangtuaResepMakananService;
import bcc.sipas.entity.OrangtuaResepMakanan;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/ortu/resepmakanan")
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orangtua ResepMakanan")
public class OrangtuaResepMakananController {

    @Autowired
    private IOrangtuaResepMakananService service;

    @Operation(description = "membuat orangtua resep makanan")
    @PreAuthorize("hasRole('ORANGTUA')")
    @PostMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> create(
            JwtAuthentication<String> jwtAuth,
            @RequestParam("resepMakananId") Long resepMakananId
    ){
        return this.service.create(Long.parseLong(jwtAuth.getId()), resepMakananId);
    }

    @Operation(description = "mendapatkan data orangtua resep makanan")
    @PreAuthorize("hasRole('ORANGTUA')")
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> get(
            JwtAuthentication<String> jwtAuth,
            @RequestParam("resepMakananId") Long resepMakananId
    ){
        return this.service.get(Long.parseLong(jwtAuth.getId()), resepMakananId);
    }

    @Operation(description = "mengupdate data orangtua resep makanan")
    @PreAuthorize("hasRole('ORANGTUA')")
    @PutMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<OrangtuaResepMakanan>>> update(
            @RequestParam("id") Long id,
            @RequestParam("remove") boolean remove
    ){
        return this.service.update(id, remove);
    }
}
