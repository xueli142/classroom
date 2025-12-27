package org.example.classroom.transfer.dto.login;

import lombok.Data;

@Data
public class ChangeUsernameDto {
    private String newUid;
    private String userId;
}
