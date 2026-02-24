package org.example.classroom.controller.user;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.login.ChangePasswordDto;
import org.example.classroom.transfer.dto.login.ChangeUsernameDto;
import org.example.classroom.transfer.dto.login.LoginDto;
import org.example.classroom.transfer.dto.login.LoginResponseDto;
import org.example.classroom.transfer.dto.simpleVo.ChangePhoneDto;
import org.example.classroom.service.user.UserService;
import org.example.classroom.util.JwtUtil;
import org.example.classroom.util.ResponseUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户认证控制器
 * <p>
 * 处理用户登录、登出、密码修改等认证相关操作
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Slf4j
@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     *
     * @param dto 登录请求 DTO
     * @return 登录响应，包含 Token 和用户信息
     */
    @PostMapping("/login")
    public ResponseDto<LoginResponseDto> login(@Valid @RequestBody LoginDto dto) {
        log.info("User login attempt: uid={}", dto.getUid());
        LoginResponseDto response = userService.login(dto);
        return ResponseUtils.success(response);
    }

    /**
     * 修改密码
     *
     * @param dto 修改密码请求 DTO
     * @return 操作结果
     */
    @PostMapping("/changePassword")
    public ResponseDto<Void> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        log.info("Password change request: uid={}", dto.getUid());
        Boolean success = userService.changePassword(dto);
        if (!success) {
            return ResponseUtils.error("密码修改失败，请检查旧密码是否正确");
        }
        return ResponseUtils.success();
    }

    /**
     * 修改用户名
     *
     * @param dto 修改用户名请求 DTO
     * @return 操作结果
     */
    @PostMapping("/changeUsername")
    public ResponseDto<Void> changeUsername(@Valid @RequestBody ChangeUsernameDto dto) {
        log.info("Username change request: userId={}", dto.getUserId());
        Boolean success = userService.changeUsername(dto);
        if (!success) {
            return ResponseUtils.error("用户名修改失败");
        }
        return ResponseUtils.success();
    }

    /**
     * 保存用户头像
     *
     * @param file   图片文件
     * @param userId 用户 ID
     * @return 操作结果
     */
    @PostMapping("/insertImage")
    public ResponseDto<Void> saveImage(@RequestParam("file") MultipartFile file,
                                       @RequestParam String userId) {
        log.info("Avatar save request: userId={}, filename={}", userId, file.getOriginalFilename());
        Boolean success = userService.saveImage(file, userId);
        if (!success) {
            return ResponseUtils.error("头像保存失败");
        }
        return ResponseUtils.success();
    }

    /**
     * 更换用户头像
     *
     * @param file    新头像文件
     * @param oldName 旧文件名
     * @param userId  用户 ID
     * @return 操作结果
     */
    @PostMapping("/changeImage")
    public ResponseDto<Void> changeImage(@RequestParam("file") MultipartFile file,
                                        @RequestParam String oldName,
                                        @RequestParam String userId) {
        log.info("Avatar change request: userId={}, oldFilename={}", userId, oldName);
        Boolean success = userService.changeImage(file, oldName, userId);
        if (!success) {
            return ResponseUtils.error("头像更换失败");
        }
        return ResponseUtils.success();
    }

    /**
     * 修改手机号
     *
     * @param dto 修改手机号请求 DTO
     * @return 操作结果
     */
    @PostMapping("/changePhone")
    public ResponseDto<Void> changePhone(@Valid @RequestBody ChangePhoneDto dto) {
        log.info("Phone change request: userId={}", dto.getUserId());
        Boolean success = userService.changePhone(dto);
        if (!success) {
            return ResponseUtils.error("手机号修改失败");
        }
        return ResponseUtils.success();
    }

    /**
     * 用户登出
     * <p>
     * 将 Token 加入黑名单，使其失效
     *
     * @param token JWT Token
     * @return 操作结果
     */
    @PostMapping("/logout")
    public ResponseDto<Void> logout(@RequestParam String token) {
        log.info("User logout request");

        if (!jwtUtil.validateToken(token)) {
            return ResponseUtils.unauthorized("Token 无效或已过期");
        }

        // 将 Token 加入黑名单
        String jti = jwtUtil.extractJti(token);
        long remain = jwtUtil.getRemainingSeconds(token);
        jwtUtil.addToBlacklist(jti, remain);

        log.info("User logged out successfully, jti={}, remainSeconds={}", jti, remain);
        return ResponseUtils.successMessage("登出成功");
    }
}