package org.example.classroom.transfer.dto.selectDto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThingBookingSelectDto {

    private Long size;
    private Long page;
    private String thingBookingId;


    private String userId;
    private java.sql.Date startTime;
    private java.sql.Date endTime;
    private Boolean active;
    private Long time;
    private String besides;

}
