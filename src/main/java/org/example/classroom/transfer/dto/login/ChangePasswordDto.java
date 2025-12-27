package org.example.classroom.transfer.dto.login;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String Uid;
}
