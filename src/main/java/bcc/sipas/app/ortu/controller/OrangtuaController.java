package bcc.sipas.app.ortu.controller;

import bcc.sipas.app.ortu.service.IOrangtuaService;
import bcc.sipas.dto.OrangtuaDto;
import bcc.sipas.entity.Orangtua;
import bcc.sipas.entity.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            @Parameter(name = "data", required = true, description = "data yang harus dikirim")
            OrangtuaDto.Create dto){
        return this.orangtuaService.create(dto);
    }
}
