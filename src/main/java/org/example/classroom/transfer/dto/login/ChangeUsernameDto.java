package org.example.classroom.transfer.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改用户名请求 DTO
 * <p>
 * 包含修改用户名时需要提供的信息
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Data
public class ChangeUsernameDto {

    /**
     * 新用户名
     */
    @NotBlank(message = "新用户名不能为空")
    private String newUid;

    /**
     * 用户 ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String userId;
}
