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

//这是一个方法泛型，通过对方法当中注入对应的实体类，
public class UserService<T extends BaseUser> {

    private final BaseUserMapper<T> userMapper;
    private final RoleMapper roleMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final String roleTag;          // 用于 JWT 里标识身份
//初始化
    public UserService(BaseUserMapper<T> userMapper,
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

        if (!userMapper.insertCheck(user)) return false;
        return roleMapper.insertBiz(user.getUid(), user.getUuid(), user.roleName());
    }

    /* --------------- 登录 --------------- */
    public LoginResponseDto login(LoginDto dto) {
        T user = userMapper.selectByUid(dto.getUid());
        if (user == null ||
                !passwordUtil.matches(dto.getPassword(), user.getPassword())) {
            return null;                       // 或抛业务异常
        }
        String jwt = jwtUtil.generateToken(
                user.getUid(), user.getName(), user.getUuid(), roleTag);
        return new LoginResponseDto(jwt, "登录成功");
    }



}