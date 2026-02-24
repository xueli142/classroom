package org.example.classroom.transfer.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码请求 DTO
 * <p>
 * 包含修改密码时需要提供的信息
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Data
public class ChangePasswordDto {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6-20个字符之间")
    private String newPassword;

    /**
     * 用户 ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String Uid;
}
