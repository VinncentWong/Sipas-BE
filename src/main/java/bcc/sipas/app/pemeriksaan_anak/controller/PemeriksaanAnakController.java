package bcc.sipas.app.pemeriksaan_anak.controller;

import bcc.sipas.app.pemeriksaan_anak.service.IPemeriksaanAnakService;
import bcc.sipas.dto.DataPemeriksaanAnakDto;
import bcc.sipas.entity.DataPemeriksaanAnak;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/pemeriksaan/anak")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pemeriksaan Anak")
@PreAuthorize("hasRole('FASKES')")
public class PemeriksaanAnakController {

    @Autowired
    private IPemeriksaanAnakService service;

    @Operation(summary = "membuat data pemeriksaan anak")
    @Parameter(name = "id", in = ParameterIn.QUERY, description = "ortu id", required = true)
    @Parameter(name = "data_anak_id", in = ParameterIn.QUERY, description = "data anak id", required = true)
    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<DataPemeriksaanAnak>>> create(
            @RequestParam("id") Long ortuId,
            @RequestParam("data_anak_id") Long dataAnakId,
            @RequestBody @Valid DataPemeriksaanAnakDto.Create dto,
            JwtAuthentication<String> jwtAuth
    ){
        return this.service.create(ortuId, Long.parseLong(jwtAuth.getId()), dataAnakId, dto);
    }

    @Operation(summary = "mendapatkan data pemeriksaan anak")
    @Parameter(name = "id", in = ParameterIn.QUERY, description = "ortu id")
    @Parameter(name = "data_anak_id", in = ParameterIn.QUERY, description = "data anak id")
    @Parameter(name = "page", in = ParameterIn.QUERY, description = "page")
    @Parameter(name = "limit", in = ParameterIn.QUERY, description = "limit")
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<List<DataPemeriksaanAnak>>>> getList(
            @RequestParam(value = "id") Long ortuId,
            @RequestParam(value = "data_anak_id") Long dataAnakId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            JwtAuthentication<String> jwtAuth
    ){
        return this.service.getList(ortuId, Long.parseLong(jwtAuth.getId()), dataAnakId, PageRequest.of(page, limit));
    }
}
