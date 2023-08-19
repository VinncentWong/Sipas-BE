package bcc.sipas.app.data_kehamilan.controller;

import bcc.sipas.app.data_kehamilan.service.IDataKehamilanService;
import bcc.sipas.dto.DataKehamilanDto;
import bcc.sipas.entity.DataKehamilan;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Data Kehamilan")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/kehamilan")
@PreAuthorize("hasRole('ORANGTUA')")
public class DataKehamilanController {

    @Autowired
    private IDataKehamilanService service;

    @Operation(description = "membuat data kehamilan")
    @ApiResponses({
            @ApiResponse(
                    description = "sukses membuat data kehamilan",
                    useReturnTypeSchema = true,
                    responseCode = "201"
            )
    })
    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<DataKehamilan>>> create(
            @Valid @RequestBody DataKehamilanDto.Create dto,
            JwtAuthentication<String> jwtAuthentication
    ){
        return this.service.create(Long.parseLong(jwtAuthentication.getId()), dto);
    }
}
