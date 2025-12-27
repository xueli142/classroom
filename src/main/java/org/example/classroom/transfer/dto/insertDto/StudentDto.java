package org.example.classroom.transfer.dto.insertDto;

import lombok.Data;

@Data
public class StudentDto {
    //特征键
    private String userId;
    // 用户相关
    private String name;
    private String password;
    private String phone;
    private String uid;
    private Boolean IsActive;
    // 学生业务相关
    private String uuid;      // 学号/学籍号
    private String grade;     // 年级
    private String school;    // 校区
    private String major;     // 专业
    private String college;   // 学院
    private String besides;   // 备注

    private String groups;


}
