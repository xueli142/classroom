package org.example.demo5.dto;

import lombok.Data;

@Data
public class SelectClassroomDto {
    private String name;
    private Integer id;
    private String classroomId;
    private String dayOfWeek;
    private String description;
    private String location;
    private Long number;
    private Boolean isActive;
}
