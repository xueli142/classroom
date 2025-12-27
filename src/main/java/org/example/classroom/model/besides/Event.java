package org.example.classroom.model.besides;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Event {

    private Long id;
    private Long number;
    private String eventName;
    private String userName;
    private String eventId;
    private String imageUrl;
    private String userId;
    private String description;
    private Boolean needAudit;
    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;
    private String besides;
}
