
package org.example.classroom.model.classroom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Classroom {

  private Long id;
  private String classroomId;
  private String location;
  private String description;
  private String imageUrl;
  @TableField(fill = FieldFill.INSERT) // 插入时填充
  private LocalDateTime createdTime;
  @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
  private LocalDateTime updatedTime;

  private String name;
  private Boolean isActive;
  private Long number;
  private String type;
  private String besides;



}
