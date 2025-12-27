package org.example.classroom.transfer.dto.besides;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public  class ClaimsPayloadDto {
    String userId;
    String role;
}
