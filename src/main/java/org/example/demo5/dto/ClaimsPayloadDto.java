package org.example.demo5.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public  class ClaimsPayloadDto {
    String uid;
    String uuid;
    String role;
}
