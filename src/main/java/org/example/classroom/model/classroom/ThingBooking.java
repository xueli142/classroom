package org.example.classroom.model.classroom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThingBooking {

  private Long id;
  private String thingBookingId;
    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;
  private String userId;
  private String thingId;
  private java.sql.Date startTime;
  private java.sql.Date endTime;
  private Boolean active;
  private Long time;
  private String besides;


}
