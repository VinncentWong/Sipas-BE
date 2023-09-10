package bcc.sipas.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class DataAnakOrtu {
    private DataAnak dataAnak;
    private Orangtua ortu;
}
