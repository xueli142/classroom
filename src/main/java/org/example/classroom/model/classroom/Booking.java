package org.example.classroom.model.classroom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {

private Long id;
private String userId;
private  String bookingId;
private String eventId;
private String bookingById;
private String classroomId;
private String status;
private String courseSlotId;
    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;



}
