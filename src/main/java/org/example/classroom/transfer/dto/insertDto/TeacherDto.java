package org.example.classroom.transfer.dto.insertDto;

import lombok.Data;

@Data
public class TeacherDto {

    private String userId;
    // 用户相关
    private String name;
    private String password;
    private String phone;
    private String uid;
    private Boolean IsActive;
    private String besides;
    //教师业务相关

   private String school;
   private String college;
    private String Uuid;
}
