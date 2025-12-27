package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

@Data
public class ClassroomSelectDto {
    private Long page;
    private Long size;
    private String location;
    private String description;
    private String name;
    private Boolean isActive;
    private Long number;
    private String type;
    private String besides;
    private String classroomId;
}
