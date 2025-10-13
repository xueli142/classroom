package org.example.demo5.service.User;

import org.example.demo5.dto.LoginDto;
import org.example.demo5.dto.LoginResponseDto;
import org.example.demo5.mapper.User.RoleMapper;
import org.example.demo5.mapper.User.BaseUserMapper;
import org.example.demo5.model.User.BaseUser;
import org.example.demo5.util.IdGeneratorUtil;
import org.example.demo5.util.JwtUtil;
import org.example.demo5.util.PasswordUtil;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public abstract class BaseService<T extends BaseUser> {

    protected final BaseUserMapper<T> userMapper;
    protected final RoleMapper roleMapper;
    protected final PasswordUtil passwordUtil;
    protected final JwtUtil jwtUtil;
    protected final String roleTag;          // STUDENT / TEACHER / ADMIN

    public BaseService(BaseUserMapper<T> userMapper,
                       RoleMapper roleMapper,
                       PasswordUtil passwordUtil,
                       JwtUtil jwtUtil,
                       String roleTag) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
        this.roleTag = roleTag;
    }

    /* --------------- 通用注册 --------------- */
    @Transactional
    public boolean register(T user) {
        user.setUuid(IdGeneratorUtil.generateUuid());
        user.setPassword(passwordUtil.encode(user.getPassword()));
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        user.setRole(roleTag);

        if (!userMapper.insertCheck(user)) return false;
        return roleMapper.insertBiz(user.getUid(), user.getUuid(), user.roleName());
    }

    /* --------------- 通用登录 --------------- */
    public LoginResponseDto login(LoginDto dto) {
        T user = userMapper.selectByUid(dto.getUid());
        if (user == null ||
                !passwordUtil.matches(dto.getPassword(), user.getPassword())) {
            return new LoginResponseDto(null, "登录失败");
        }
        String jwt = jwtUtil.generateToken(user.getUid(), user.getName(),
                user.getUuid(), roleTag);
        return new LoginResponseDto(jwt, "登录成功");
    }
}