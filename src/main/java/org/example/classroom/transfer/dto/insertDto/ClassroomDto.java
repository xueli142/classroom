package org.example.classroom.transfer.dto.insertDto;

import lombok.Data;

@Data
public class ClassroomDto {
    private String classroomId;
    private String location;
    private String description;
    private String name;
    private Boolean isActive;
    private Long number;
    private String type;
    private String besides;

}
