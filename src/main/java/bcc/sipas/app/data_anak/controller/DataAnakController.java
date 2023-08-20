package bcc.sipas.app.data_anak.controller;

import bcc.sipas.app.data_anak.service.IDataAnakService;
import bcc.sipas.dto.DataAnakDto;
import bcc.sipas.entity.DataAnak;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import bcc.sipas.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Data Anak")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ORANGTUA', 'FASKES')")
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

    @Operation(description = "mendapatkan list data anak")
    @ApiResponses({
            @ApiResponse(
                    description = "sukses mendapatkan data anak",
                    useReturnTypeSchema = true,
                    responseCode = "200"
            )
    })
    @Parameter(name = "id", in = ParameterIn.QUERY, required = true)
    @Parameter(description = "page", in = ParameterIn.QUERY, name = "page")
    @Parameter(description = "limit", in = ParameterIn.QUERY, name = "limit")
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<List<DataAnak>>>> getList(
            @RequestParam("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "1") Integer limit
    ){
        return this.service.getList(id, PageRequest.of(page, limit));
    }
}
