package org.example.classroom.transfer.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO
 * <p>
 * 包含用户登录时需要提供的账号和密码信息
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Data
public class LoginDto {

    /**
     * 用户账号（学号/工号/用户名）
     */
    @NotBlank(message = "账号不能为空")
    private String uid;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
