package org.example.classroom.transfer.dto.login;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.classroom.model.user.User;

/**
 * 登录响应 DTO
 * <p>
 * 包含登录成功后返回的 Token、消息和用户信息
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    /**
     * JWT Token
     */
    private String token;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 用户信息
     */
    private User user;
}
