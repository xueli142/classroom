package org.example.classroom.model.classroom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Term {
    private Long id;
    private String termId;
    private Date startTime;
    private Date endTime;
    private Long weekNow;
    private String termName;
    private Boolean IsActive;
    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;

}
