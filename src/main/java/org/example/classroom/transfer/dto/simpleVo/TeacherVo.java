package org.example.classroom.transfer.dto.simpleVo;

import lombok.Data;

@Data
public class TeacherVo extends BaseUserVo {

    // 教师业务相关
    private String uuid;        // 教师编号
    private String college;     // 所属学院
    private String school;

    private String besides;     // 备注
}
