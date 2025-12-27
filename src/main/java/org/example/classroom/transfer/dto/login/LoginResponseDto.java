package org.example.classroom.transfer.dto.login;


import lombok.Data;
import org.example.classroom.model.user.User;

@Data
public class LoginResponseDto {
    private String token;
    private String message;
    private User user;

}
