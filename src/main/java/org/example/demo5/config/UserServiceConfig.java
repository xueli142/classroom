package org.example.demo5.config;

import org.example.demo5.mapper.User.RoleMapper;
import org.example.demo5.mapper.User.AdminMapper;
import org.example.demo5.mapper.User.StudentMapper;
import org.example.demo5.mapper.User.TeacherMapper;
import org.example.demo5.model.User.Admin;
import org.example.demo5.model.User.Student;
import org.example.demo5.model.User.Teacher;
import org.example.demo5.service.User.BaseService;
import org.example.demo5.util.JwtUtil;
import org.example.demo5.util.PasswordUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration                      // ① 标记 “配置类” —— 替代 XML
public class UserServiceConfig {

    @Bean("studentService")         // ② 生成第 1 个 Bean，名字 = studentService
    public BaseService<Student> studentService(
            StudentMapper mapper,        // ③ Spring 按类型自动注入
            RoleMapper roleMapper,
            PasswordUtil passwordUtil,
            JwtUtil jwtUtil) {
        // ④ 把具体 Mapper 传进去 → 得到专门操作 student 表的服务
        return new BaseService<>(mapper, roleMapper, passwordUtil, jwtUtil, "STUDENT");
    }

    @Bean("teacherService")         // ⑤ 第 2 个 Bean
    public BaseService<Teacher> teacherService(TeacherMapper mapper,
                                               RoleMapper roleMapper,
                                               PasswordUtil passwordUtil,
                                               JwtUtil jwtUtil) {
        return new BaseService<>(mapper, roleMapper, passwordUtil, jwtUtil, "TEACHER");
    }

    @Bean("adminService")           // ⑥ 第 3 个 Bean
    public BaseService<Admin> adminService(AdminMapper mapper,
                                           RoleMapper roleMapper,
                                           PasswordUtil passwordUtil,
                                           JwtUtil jwtUtil) {
        return new BaseService<>(mapper, roleMapper, passwordUtil, jwtUtil, "ADMIN");
    }
}