package bcc.sipas.entity;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class OpenApiClientResponse {
    private List<String> messages;
    private List<String> responses;
}
