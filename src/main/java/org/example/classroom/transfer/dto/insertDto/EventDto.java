package org.example.classroom.transfer.dto.insertDto;

import lombok.Data;

@Data
public class EventDto {
    private Long number;
    private String eventId;
    private String userId;
    private String eventName;
    private String besides;
    private String userName;   // 对应 CourseDto 的 teacherName
    private String description;
    private Boolean needAudit;
}