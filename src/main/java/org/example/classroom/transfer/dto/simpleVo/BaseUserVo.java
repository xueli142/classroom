package org.example.classroom.transfer.dto.simpleVo;

import lombok.Data;
//用户表的基础信息
@Data
public class BaseUserVo {



    private String userId;
    private String name;
    private String role;
    private String phone;
    private String uid;//登录使用的用户名
    private Boolean isActive;
    private String imageUrl;




}
