package org.example.classroom.model.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("t_user")
@Data
public class User {

  private Long id;
  private String userId;
  private String name;
  private String phone;
  private String password;

  private String uid;
  private String role;
  private String imageUrl;
  @TableField("is_active")
  private Boolean IsActive;
  private String besides;
    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;


}
