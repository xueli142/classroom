package org.example.demo5.model.Classroom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Classroom {

  private long id;
  private String classroomId;
  private String name;
  private long number;
  private String description;
  private String location;
  private Boolean isActive;
  private Long dayOfWeek;
  @TableField(fill = FieldFill.INSERT) // 插入时填充
  private LocalDateTime createdTime;

  @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
  private LocalDateTime updatedTime;

}
