package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

@Data
public class BookingSelectDto {

    private Long size;
    private  Long Page;
    private String userId;
    private  String bookingId;
    private String eventId;
    private String bookingById;
    private String classroomId;
    private Boolean status;
    private String courseSlotId;

}
