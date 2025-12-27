package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

//查询信息，输入这些进行筛选，注意是分页查询
@Data
public class StudentSelectDto extends BaseSelectDto {

    private String major;
    private String uuid;//学号
    private String grade;
    private String school;
    private String college;

    private String groups;

}
