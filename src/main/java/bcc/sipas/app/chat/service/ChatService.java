package bcc.sipas.app.chat.service;

import bcc.sipas.app.chat.repository.ChatMessageRepository;
import bcc.sipas.app.chat.repository.ChatRepository;
import bcc.sipas.app.chat.repository.ChatResponseRepository;
import bcc.sipas.constant.OpenApiConstant;
import bcc.sipas.constant.SecurityConstant;
import bcc.sipas.entity.*;
import bcc.sipas.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ChatService implements IChatService{

    /*
    chat usage repository
     */
    @Autowired
    private ChatRepository repository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatResponseRepository chatResponseRepository;

    @Value("${openai.key}")
    private String apiKey;

    @Override
    public Mono<ResponseEntity<Response<OpenApiResponse>>> create(Long ortuId, String message) {
        OpenApiRequest req = OpenApiRequest
                .builder()
                .model(OpenApiConstant.MODEL)
                .messages(List.of(
                        new OpenApiRequest.Payload(OpenApiConstant.ROLE, message + OpenApiConstant.NOTES)
                ))
                .build();
        return WebClient.create(OpenApiConstant.URL)
                .post()
                .headers((h) -> {
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.add(HttpHeaders.AUTHORIZATION, String.format("%s %s", SecurityConstant.BEARER, this.apiKey));
                })
                .body(Mono.just(req), OpenApiResponse.class)
                .retrieve()
                .bodyToMono(OpenApiResponse.class)
                .flatMap((d) -> {
                    ChatMessage chatMessage = ChatMessage
                            .builder()
                            .message(message)
                            .fkOrtuId(ortuId)
                            .createdAt(LocalDate.now())
                            .build();
                    ChatResponse chatResponse = ChatResponse
                            .builder()
                            .response(d.getChoices().get(0).message().content())
                            .fkOrtuId(ortuId)
                            .createdAt(LocalDate.now())
                            .build();
                    Mono<ChatMessage> chatMessageMono = this.chatMessageRepository.save(chatMessage);
                    Mono<ChatResponse> chatResponseMono = this.chatResponseRepository.save(chatResponse);
                    return Mono.zip(chatMessageMono, chatResponseMono)
                            .map((v) -> this.repository.save(
                                    ChatResponseUsage
                                            .builder()
                                            .totalTokens(d.getUsage().totalTokens())
                                            .fkChatResponse(v.getT2().getId())
                                            .fkOrtuId(ortuId)
                                            .createdAt(LocalDate.now())
                                            .build())
                            )
                            .flatMap((v) -> v)
                            .then(Mono.fromCallable(() -> ResponseUtil.sendResponse(
                                    HttpStatus.OK,
                                    Response
                                            .<OpenApiResponse>builder()
                                            .success(true)
                                            .message("sukses membuat pertanyaan ke open ai")
                                            .data(d)
                                            .build()
                            )));
                });
    }

    @Override
    public Mono<ResponseEntity<Response<OpenApiClientResponse>>> get(Long ortuId) {
        Mono<List<ChatMessage>> chatMessageFlux = this.chatMessageRepository.findByOrtuId(ortuId).collectList();
        Mono<List<ChatResponse>> chatResponseFlux = this.chatResponseRepository.findByOrtuId(ortuId).collectList();
        return Mono.zip(
                Mono.from(chatMessageFlux),
                Mono.from(chatResponseFlux)
        )
                .map((v) -> {
                    List<ChatMessage> listMessage = v.getT1();
                    List<ChatResponse> listResponse = v.getT2();
                    Map<LocalDate, List<ChatMessage>> mapMessage = new LinkedHashMap<>();
                    Map<LocalDate, List<ChatResponse>> mapResponse = new LinkedHashMap<>();
                    listMessage.forEach((message) -> {
                        var value = mapMessage.get(message.getCreatedAt());
                        if(value == null){
                            var mapListValue = new ArrayList<ChatMessage>();
                            mapListValue.add(message);
                            mapMessage.put(message.getCreatedAt(), mapListValue);
                        } else {
                            value.add(message);
                        }
                    });
                    listResponse.forEach((response) -> {
                        var value = mapResponse.get(response.getCreatedAt());
                        if(value == null){
                            var mapListValue = new ArrayList<ChatResponse>();
                            mapListValue.add(response);
                            mapResponse.put(response.getCreatedAt(), mapListValue);
                        } else {
                            value.add(response);
                        }
                    });
                    return List.of(mapMessage, mapResponse);
                })
                .map((v) -> {
                    var mapValue = v.get(0);
                    var mapResponse = v.get(1);
                    List<? extends List<?>> listListChatMessage = mapValue.values().stream().toList();
                    List<? extends List<?>> listListChatResponse = mapResponse.values().stream().toList();
                    return ResponseUtil.sendResponse(
                            HttpStatus.OK,
                            Response
                                    .<OpenApiClientResponse>builder()
                                    .message("sukses mendapatkan history chat")
                                    .success(true)
                                    .data(
                                            OpenApiClientResponse
                                                    .builder()
                                                    .messages(listListChatMessage)
                                                    .responses(listListChatResponse)
                                                    .build()
                                    )
                                    .build()
                    );
                });
    }
}
