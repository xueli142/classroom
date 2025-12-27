package org.example.classroom.model.classroom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


@Data
public class Thing {

  private Long id;
  private String classroomId;
    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;
  private String type;
  private String thingName;
  private String thingId;
  private Boolean needBooking;
  private Boolean needUse;
  private String besides;
  private String description;



}
