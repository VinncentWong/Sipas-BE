package bcc.sipas.app.ortu.controller;

import bcc.sipas.app.ortu.service.IOrangtuaService;
import bcc.sipas.dto.OrangtuaDto;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orangtua")
@Tag(name = "Orangtua")
public class OrangtuaController {

    @Autowired
    private IOrangtuaService orangtuaService;

    @Operation(description = "membuat data orangtua")
    @ApiResponses({
            @ApiResponse(
                    description = "sukses membuat orangtua",
                    useReturnTypeSchema = true,
                    responseCode = "201"
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<Response<Orangtua>>> create(
            @Valid
            @RequestBody
            OrangtuaDto.Create dto){
        return this.orangtuaService.create(dto);
    }

    @Operation(description = "orangtua login")
    @ApiResponses({
            @ApiResponse(
                    description = "sukses login orangtua",
                    useReturnTypeSchema = true,
                    responseCode = "200"
            )
    })
    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<Response<Orangtua>>> login(
            @Valid
            @RequestBody
            OrangtuaDto.Login dto){
        return this.orangtuaService.login(dto);
    }

    @Operation(description = "mendapatkan data orangtua")
    @ApiResponses({
            @ApiResponse(
                    description = "sukses mendapatkan data orangtua",
                    useReturnTypeSchema = true,
                    responseCode = "200"
            )
    })
    @Parameter(name = "id", required = true, in = ParameterIn.PATH)
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('FASKES', 'ORANGTUA')")
    @SecurityRequirement(name = "bearerAuth")
    public Mono<ResponseEntity<Response<Orangtua>>> get(@PathVariable("id") Long id){
        return this.orangtuaService.get(id);
    }
}
