package org.example.classroom.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码工具类，使用BCryptPasswordEncoder进行密码加密和匹配
 * 该类被标记为Spring的组件，可以被Spring容器管理和注入
 */
@Component

public class PasswordUtil {
    // 使用BCryptPasswordEncoder作为加密器，final表示该对象不可更改
    private  final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    /**
     * 对原始密码进行加密
     * @param rawPassword 原始密码
     * @return 加密后的密码字符串
     */
    public  String encode(String rawPassword){
        return encoder.encode(rawPassword);
    }

    /**
     * 验证原始密码与加密密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 如果密码匹配返回true，否则返回false
     */
    public  boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
