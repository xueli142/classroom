package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

@Data
public class TeacherSelectDto extends BaseSelectDto {


    private String uuid;//学号
    private String school;
    private String college;



}
