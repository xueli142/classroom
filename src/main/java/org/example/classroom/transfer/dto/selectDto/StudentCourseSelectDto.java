package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

@Data
public class StudentCourseSelectDto {
    private Long size;
    private Long page;
    private Boolean status;
    private String courseId;
    private String studentCourseId;
    private String userId;


}
