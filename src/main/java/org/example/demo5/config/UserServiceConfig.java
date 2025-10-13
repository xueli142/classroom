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


}