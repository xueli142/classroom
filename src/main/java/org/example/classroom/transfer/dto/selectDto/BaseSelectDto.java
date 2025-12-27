package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

@Data
public class BaseSelectDto {
    private Long size;
    private Long page;
    private String name;
    private String phone;
    private String role;
    private String uid;
    private Boolean isActive;
    private String userId;

}
