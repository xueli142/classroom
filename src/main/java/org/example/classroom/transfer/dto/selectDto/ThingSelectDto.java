package org.example.classroom.transfer.dto.selectDto;
import lombok.Data;
@Data
public class ThingSelectDto {



    private Long size;
    private Long page;
    private String classroomId;
    private String type;
    private String thingName;
    private String thingId;
    private Boolean needBooking;
    private Boolean needUse;
    private String besides;
    private String description;
    private String urlImage;



}
