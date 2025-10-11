package org.example.demo5.model.Classroom;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Data
public class ClassroomImages {

  private long id;
  private String classroomId;
  private String imageUrl;
  private String imageName;

  @TableField(fill = FieldFill.INSERT) // 插入时填充
  private LocalDateTime createdTime;

  @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
  private LocalDateTime updatedTime;


}
