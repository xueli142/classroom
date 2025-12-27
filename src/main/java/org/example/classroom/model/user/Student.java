package org.example.classroom.model.user;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Student {

  private Long id;
  private String userId;
  private String uuid;
  private String grade;
  private String school;
  private String major;
  private String college;
  @TableField(fill = FieldFill.INSERT) // 插入时填充
  private LocalDateTime createdTime;
  @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
  private LocalDateTime updatedTime;
  private String besides;

  private String groups;


}
