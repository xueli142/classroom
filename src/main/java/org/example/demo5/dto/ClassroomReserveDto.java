package org.example.demo5.dto;

import lombok.Data;

@Data
public class ClassroomReserveDto {
    private String classroomId;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;
    private String purpose;
    private String uid;
}
