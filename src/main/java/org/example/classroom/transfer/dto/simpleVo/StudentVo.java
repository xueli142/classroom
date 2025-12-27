package org.example.classroom.transfer.dto.simpleVo;

import lombok.Data;

//继承自用户表的附加信息，用于返回给前端
@Data
public class   StudentVo extends BaseUserVo {


    private String uuid;//学号
    private String grade;
    private String major;
    private String school;
    private String college;

    private String groups;


}
