package org.example.demo5.service.User;

import org.example.demo5.mapper.User.AdminMapper;
import org.example.demo5.mapper.User.RoleMapper;
import org.example.demo5.mapper.User.AdminMapper;
import org.example.demo5.model.User.Admin;
import org.example.demo5.model.User.Teacher;
import org.example.demo5.util.JwtUtil;
import org.example.demo5.util.PasswordUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends BaseService<Admin>{
    private final AdminMapper adminMapper;
              // ⑥ 第 3 个 Bean
    public AdminService(AdminMapper adminMapper,
                          RoleMapper roleMapper,
                          PasswordUtil passwordUtil,
                          JwtUtil jwtUtil) {
        super(adminMapper, roleMapper, passwordUtil, jwtUtil, "TEACHER");
        this.adminMapper = adminMapper;
    }

}
