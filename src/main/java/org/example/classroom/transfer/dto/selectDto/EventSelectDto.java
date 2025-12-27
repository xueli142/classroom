package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSelectDto {
    private Long size;
    private Long page;
    private Long number;
    private String eventName;
    private String userName;   // 对应 CourseSelectDto 的 teacherName
    private String eventId;
    private String userId;
    private String description;
    private Boolean needAudit;
    private String besides;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}