package bcc.sipas.app.pemeriksaan_kehamilan.controller;

import bcc.sipas.app.pemeriksaan_kehamilan.service.IPemeriksaanKehamilanService;
import bcc.sipas.dto.DataPemeriksaanKehamilanDto;
import bcc.sipas.entity.DataPemeriksaanKehamilan;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Pemeriksaan Kehamilan")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/hasil/pemeriksaan")
@PreAuthorize("hasRole('FASKES')")
public class PemeriksaanKehamilanController {

    @Autowired
    private IPemeriksaanKehamilanService service;

    @Operation(summary = "membuat data pemeriksaan kehamilan")
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "orangtua id")
    @PostMapping(
            value = "/{id}",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<DataPemeriksaanKehamilan>>> create(
            @PathVariable("id") Long id,
            @Valid @RequestBody DataPemeriksaanKehamilanDto.Create dto,
            JwtAuthentication<String> jwtAuth
            ){
        return this.service.create(id, Long.parseLong(jwtAuth.getId()), dto);
    }
}