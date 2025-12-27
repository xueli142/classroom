package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseSelectDto {
    private Long size;
    private Long page;
    private Long number;
    private String courseName;
    private String teacherName;
    private String courseId;
    private String userId;
    private String description;
    private Boolean needAudit;
    private String besides;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
