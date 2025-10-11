package org.example.demo5.dto;

import lombok.Data;

@Data
public class ClassroomDto {
    private String name;
    private String location;
    private long number;
    private String description;
    private Long typeId;
    private Long dayOfWeek;
}
