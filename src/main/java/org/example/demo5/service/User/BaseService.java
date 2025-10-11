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

public class BaseService<T extends BaseUser> {

    private final BaseUserMapper<T> userMapper;
    private final RoleMapper roleMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final String roleTag;          // 用于 JWT 里标识身份

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

    /* --------------- 注册（含重复检查）--------------- */
    @Transactional
    public boolean register(T user) {
        user.setUuid(IdGeneratorUtil.generateUuid());
        user.setPassword(passwordUtil.encode(user.getPassword()));
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        user.setRole(this.roleTag);

        if (!userMapper.insertCheck(user)) return false;
        //userMapper.insert(user);
        return roleMapper.insertBiz(user.getUid(), user.getUuid(), user.roleName());

    }

    /* --------------- 登录 --------------- */
    public LoginResponseDto login(LoginDto dto) {
        T user = userMapper.selectByUid(dto.getUid());
        if (user == null ||
                !passwordUtil.matches(dto.getPassword(), user.getPassword())) {
            return new LoginResponseDto(null,"登录失败");                       // 或抛业务异常
        }
        String jwt = jwtUtil.generateToken(
                user.getUid(), user.getName(), user.getUuid(), roleTag);
        return new LoginResponseDto(jwt, "登录成功");
    }
}