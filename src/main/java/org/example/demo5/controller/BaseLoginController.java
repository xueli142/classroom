package org.example.demo5.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo5.dto.LoginDto;
import org.example.demo5.dto.LoginResponseDto;
import org.example.demo5.model.User.Admin;
import org.example.demo5.model.User.Student;
import org.example.demo5.model.User.Teacher;
import org.example.demo5.service.User.BaseService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor   // lombok 生成构造器，Spring 再按构造器注入
public class BaseLoginController {

    private final BaseService<Student> studentService;   // 对应 @Bean("studentService")
    private final BaseService<Teacher> teacherService;
    private final BaseService<Admin>   adminService;

    @PostMapping("/login/student")
    public LoginResponseDto studentLogin(@RequestBody LoginDto dto) {
        return studentService.login(dto);
    }
    @PostMapping("/register/student")
     public boolean studentRegister(@RequestBody Student student){
        return studentService.register(student);}

    @PostMapping("/login/teacher")
    public LoginResponseDto teacherLogin(@RequestBody LoginDto dto) {
        return teacherService.login(dto);

    }
    @PostMapping("/register/teacher")
     public boolean teacherRegister(@RequestBody Teacher teacher){
        return teacherService.register(teacher);}

    @PostMapping("/login/admin")
    public LoginResponseDto adminLogin(@RequestBody LoginDto dto) {
        return adminService.login(dto);
    }
    @PostMapping("/register/admin")
    public boolean adminRegister(@RequestBody Admin admin){
        return adminService.register(admin);}

}