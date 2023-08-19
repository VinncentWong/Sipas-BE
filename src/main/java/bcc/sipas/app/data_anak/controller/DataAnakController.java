package bcc.sipas.app.data_anak.controller;

import bcc.sipas.app.data_anak.service.IDataAnakService;
import bcc.sipas.dto.DataAnakDto;
import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import bcc.sipas.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Data Anak")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ORANGTUA')")
@RestController
@RequestMapping("/anak")
@Slf4j
public class DataAnakController {

    @Autowired
    private IDataAnakService service;

    @Operation(description = "membuat data anak")
    @ApiResponses({
            @ApiResponse(
                    description = "sukses membuat data anak",
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
    public Mono<ResponseEntity<Response<DataAnak>>> create(
            @Valid @RequestBody DataAnakDto.Create dto,
            JwtAuthentication<String> jwtAuthentication
            ){
        return this.service.create(Long.parseLong(jwtAuthentication.getId()), dto);
    }
}
