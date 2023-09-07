package bcc.sipas.app.resep_makanan.controller;

import bcc.sipas.app.resep_makanan.service.IResepMakananService;
import bcc.sipas.dto.ResepMakananDto;
import bcc.sipas.entity.ResepMakanan;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/resep/makanan")
@Tag(name = "Resep Makanan")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class ResepMakananController {

    @Autowired
    private IResepMakananService service;

    @Operation(description = "membuat resep makanan")
    @PreAuthorize("hasRole('FASKES')")
    @PostMapping(
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<ResepMakanan>>> create(
            JwtAuthentication<String> jwtAuth,
            @RequestPart("image") Mono<FilePart> image,
            @RequestPart("dto") ResepMakananDto.Create dto
            ){
        return this.service.create(Long.parseLong(jwtAuth.getId()), dto, image);
    }

    @Operation(description = "mendapatkan list data resep makanan")
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    @Parameter(name = "limit", in = ParameterIn.QUERY, required = true)
    @Parameter(name = "page", in = ParameterIn.QUERY, required = true)
    public Mono<ResponseEntity<Response<List<ResepMakanan>>>> getList(
            JwtAuthentication<String> jwtAuth,
            @RequestParam(value = "limit", defaultValue = "10") Long limit,
            @RequestParam(value = "page", defaultValue = "0") Long page
    ){
        return this.service.getList(Long.parseLong(jwtAuth.getId()), PageRequest.of(page.intValue(), limit.intValue()));
    }

    @Operation(description = "mendapatkan list data resep makanan")
    @DeleteMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "id resep makanan")
    public Mono<ResponseEntity<Response<Void>>> delete(
            @PathVariable("id") Long id
    ){
        return this.service.delete(id);
    }

    @Operation(description = "mendapatkan jumlah resep makanan")
    @GetMapping(
            value = "/count",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<Long>>> count(
            JwtAuthentication<String> jwtAuth
    ){
        return this.service.count(Long.parseLong(jwtAuth.getId()));
    }
}