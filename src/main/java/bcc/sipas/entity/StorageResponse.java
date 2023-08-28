package bcc.sipas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorageResponse {
    private String publicId;
    private String signature;
    private String secureUrl;
}
