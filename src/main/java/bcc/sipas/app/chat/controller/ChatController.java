package bcc.sipas.app.chat.controller;

import bcc.sipas.app.chat.service.IChatService;
import bcc.sipas.dto.ChatDto;
import bcc.sipas.entity.OpenApiResponse;
import bcc.sipas.entity.Response;
import bcc.sipas.security.authentication.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Chat with OpenAI")
@RestController
@RequestMapping("/chat")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ORANGTUA')")
public class ChatController {

    @Autowired
    private IChatService service;

    @Operation(description = "melakukan chat dengan open ai")
    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public Mono<ResponseEntity<Response<OpenApiResponse>>> create(
            @RequestBody @Valid ChatDto.Create dto,
            JwtAuthentication<String> jwtAuthentication
    ){
        return this.service.create(Long.parseLong(jwtAuthentication.getId()), dto.message());
    }
}
