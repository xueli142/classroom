package org.example.demo5.dto;

import lombok.Data;

@Data
public class TimeDto {
    private String classroomId;
    private String dayOfWeek;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;
}
