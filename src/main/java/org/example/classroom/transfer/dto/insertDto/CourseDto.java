package org.example.classroom.transfer.dto.insertDto;

import lombok.Data;

@Data
public class CourseDto {
    private Long number;
    private String courseId;
    private String userId;
    private String courseName;
    private String besides;
    private String teacherName;
    private String description;
    private Boolean needAudit;
}
