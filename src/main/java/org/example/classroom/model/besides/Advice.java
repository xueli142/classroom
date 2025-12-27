package org.example.classroom.model.besides;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Advice {
 private Long id;
 private String adviceId;
 private String name;
 private String userId;
 private String advice;
 private String title;
 private String type;
 private Boolean status;
    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;


}
