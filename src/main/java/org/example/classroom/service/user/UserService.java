package org.example.classroom.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.classroom.constant.BusinessConstants;
import org.example.classroom.dao.user.UserMapper;
import org.example.classroom.exception.auth.AuthenticationException;
import org.example.classroom.exception.business.UserNotFoundException;
import org.example.classroom.transfer.dto.login.ChangePasswordDto;
import org.example.classroom.transfer.dto.login.ChangeUsernameDto;
import org.example.classroom.transfer.dto.login.LoginDto;
import org.example.classroom.transfer.dto.login.LoginResponseDto;
import org.example.classroom.transfer.dto.selectDto.AdminSelectDto;
import org.example.classroom.transfer.dto.simpleVo.ChangePhoneDto;
import org.example.classroom.model.user.User;
import org.example.classroom.util.ImageUtil;
import org.example.classroom.util.JwtUtil;
import org.example.classroom.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户服务类
 * <p>
 * 处理用户相关的业务逻辑，包括登录、密码修改、头像管理等
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordUtil passwordUtil;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ImageUtil imageUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${announcement.image-path}")
    private String accessUrlPrefix;

    /**
     * 用户登录
     *
     * @param dto 登录请求 DTO
     * @return 登录响应，包含 Token 和用户信息
     * @throws AuthenticationException 用户名或密码错误时抛出
     */
    public LoginResponseDto login(LoginDto dto) {
        String uid = dto.getUid();

        User user = userMapper.selectUserByUid(uid);
        if (user == null || !passwordUtil.matches(dto.getPassword(), user.getPassword())) {
            throw AuthenticationException.invalidCredentials();
        }

        if (!user.getIsActive()) {
            throw AuthenticationException.userDisabled(uid);
        }

        String jwt = jwtUtil.generateToken(user.getUserId(), user.getRole());
        log.info("User login successful: uid={}, userId={}", uid, user.getUserId());

        return LoginResponseDto.builder()
                .token(jwt)
                .message("登录成功")
                .user(user)
                .build();
    }

    /**
     * 修改密码
     *
     * @param dto 修改密码请求 DTO
     * @return 是否成功
     */
    public Boolean changePassword(ChangePasswordDto dto) {
        User user = userMapper.selectUserByUid(dto.getUid());
        if (user == null) {
            return false;
        }

        if (!passwordUtil.matches(dto.getOldPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(passwordUtil.encode(dto.getNewPassword()));
        boolean success = userMapper.updateByUserId(user);
        if (success) {
            log.info("Password changed successfully: uid={}", dto.getUid());
        }
        return success;
    }

    /**
     * 修改用户名
     *
     * @param dto 修改用户名请求 DTO
     * @return 是否成功
     */
    public Boolean changeUsername(ChangeUsernameDto dto) {
        User user = userMapper.selectUserByUid(dto.getUserId());
        if (user == null) {
            return false;
        }

        user.setUid(dto.getNewUid());
        boolean success = userMapper.updateByUserId(user);
        if (success) {
            log.info("Username changed successfully: userId={}, newUid={}", dto.getUserId(), dto.getNewUid());
        }
        return success;
    }

    /**
     * 修改手机号
     *
     * @param dto 修改手机号请求 DTO
     * @return 是否成功
     */
    public Boolean changePhone(ChangePhoneDto dto) {
        User user = userMapper.selectUserByUid(dto.getUserId());
        if (user == null) {
            return false;
        }

        user.setPhone(dto.getPhone());
        boolean success = userMapper.updateByUserId(user);
        if (success) {
            log.info("Phone changed successfully: userId={}", dto.getUserId());
        }
        return success;
    }

    /**
     * 保存用户头像
     *
     * @param file   图片文件
     * @param userId 用户 ID
     * @return 是否成功
     */
    public Boolean saveImage(MultipartFile file, String userId) {
        User user = userMapper.selectUserByUid(userId);
        if (user == null) {
            return false;
        }

        String url = imageUtil.saveImage(file);
        user.setImageUrl(url);

        boolean success = userMapper.updateByUserId(user);
        if (success) {
            log.info("Avatar saved successfully: userId={}", userId);
        }
        return success;
    }

    /**
     * 更换用户头像
     *
     * @param file    新头像文件
     * @param oldName 旧文件名
     * @param userId  用户 ID
     * @return 是否成功
     */
    public Boolean changeImage(MultipartFile file, String oldName, String userId) {
        User user = userMapper.selectUserByUid(userId);
        if (user == null) {
            return false;
        }

        String url = imageUtil.changeImage(file, oldName);
        user.setImageUrl(url);

        boolean success = userMapper.updateByUserId(user);
        if (success) {
            log.info("Avatar changed successfully: userId={}", userId);
        }
        return success;
    }

    /**
     * 删除用户（单个）
     *
     * @param userId 用户 ID
     * @return 是否成功
     */
    public Boolean deleteByUserId(String userId) {
        String url = userMapper.selectUrlByUserId(userId);
        if (url == null) {
            return false;
        }

        boolean dbResult = userMapper.deleteByUserId(userId);
        boolean fileResult = imageUtil.deleteImage(url);

        if (!dbResult || !fileResult) {
            return false;
        }

        log.info("User deleted successfully: userId={}", userId);
        return true;
    }

    /**
     * 批量删除用户
     *
     * @param userIds 用户 ID 列表
     * @return 是否成功
     */
    public Boolean deleteByUserIds(List<String> userIds) {
        boolean dbResult = userMapper.deleteByUserIds(userIds);
        List<String> urls = userMapper.selectUrlByUserIds(userIds);
        boolean fileResult = imageUtil.deleteImages(urls);

        if (!dbResult || !fileResult) {
            return false;
        }

        log.info("Batch delete users successful: count={}", userIds.size());
        return true;
    }

    /**
     * 用户分页查询
     *
     * @param dto 查询条件 DTO
     * @return 分页结果
     */
    public IPage<User> pageUser(AdminSelectDto dto) {
        long page = dto.getPage() == null ? BusinessConstants.DEFAULT_PAGE_NUMBER : dto.getPage();
        long size = dto.getSize() == null ? BusinessConstants.DEFAULT_PAGE_SIZE : dto.getSize();

        Page<User> pg = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 姓名模糊查询
        wrapper.like(StringUtils.hasText(dto.getName()), User::getName, dto.getName());
        // 手机号模糊查询
        wrapper.like(StringUtils.hasText(dto.getPhone()), User::getPhone, dto.getPhone());
        // 角色精确查询
        wrapper.eq(StringUtils.hasText(dto.getRole()), User::getRole, dto.getRole());
        // uid 精确查询
        wrapper.eq(StringUtils.hasText(dto.getUid()), User::getUid, dto.getUid());
        // userId 精确查询
        wrapper.eq(StringUtils.hasText(dto.getUserId()), User::getUserId, dto.getUserId());
        // 状态精确查询
        wrapper.eq(dto.getIsActive() != null, User::getIsActive, dto.getIsActive());

        return userMapper.selectPage(pg, wrapper);
    }
}
