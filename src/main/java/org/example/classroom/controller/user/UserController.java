package org.example.classroom.controller.user;


import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.login.ChangePasswordDto;
import org.example.classroom.transfer.dto.login.ChangeUsernameDto;
import org.example.classroom.transfer.dto.login.LoginDto;
import org.example.classroom.transfer.dto.login.LoginResponseDto;
import org.example.classroom.transfer.dto.simpleVo.ChangePhoneDto;
import org.example.classroom.service.user.UserService;
import org.example.classroom.util.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/auth/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto dto){
        return userService.login(dto);
    }

@PostMapping("/changePassword")
    public Boolean ChangePassword(@RequestBody ChangePasswordDto dto){
        return userService.changePassword(dto);
    }

@PostMapping("/changeUsername")
    public Boolean ChangeUsername(@RequestBody ChangeUsernameDto dto){
        return userService.changeUsername(dto);
    }

    @PostMapping("/insertImage")
    public Boolean img(@RequestParam("file") MultipartFile file,@RequestParam String userId) {
        return userService.saveImage(file,userId);
    }

    @PostMapping("changeImage")
    public Boolean changeImg(@RequestParam("file") MultipartFile file,@RequestParam String oldName,@RequestParam String userId){
        return userService.changeImage(file,oldName,userId);
    }

    @PostMapping("/changePhone")
    public Boolean changePhone (ChangePhoneDto dto){
        return userService.changePhone(dto);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestParam String token) {


        if (!jwtUtil.validateToken(token)) {
            return Map.of("code", 401, "msg", "Invalid or expired token");
        }

        // 2. 加入黑名单
        String jti = jwtUtil.extractJti(token);
        long remain = jwtUtil.getRemainingSeconds(token);
        jwtUtil.addToBlacklist(jti, remain);

        return Map.of("code", 200, "msg", "Logout successful");
    }


}