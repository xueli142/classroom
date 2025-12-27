package org.example.classroom.transfer.dto.insertDto;

import lombok.Data;


@Data
public class ThingDto {

    private String classroomId;


    private String type;
    private String thingName;
    private String thingId;
    private Boolean needBooking;
    private Boolean needUse;
    private String besides;
    private String description;

}
