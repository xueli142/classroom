package org.example.classroom.transfer.dto.insertDto;
import lombok.Data;



@Data
public class ThingBookingDto {



private String thingId;
    private String thingBookingId;
    private String userId;
    private java.sql.Date startTime;
    private java.sql.Date endTime;
    private Boolean active;
    private Long time;
    private String besides;




}
