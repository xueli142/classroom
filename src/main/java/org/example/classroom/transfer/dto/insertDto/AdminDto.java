package org.example.classroom.transfer.dto.insertDto;

import lombok.Data;

@Data
public class AdminDto {

    private String userId;
    // 用户相关
    private String name;
    private String password;
    private String phone;
    private String uid;
    private Boolean IsActive;
    private String realName;
    private String email;
    private String createBy;
    private String besides;

}
