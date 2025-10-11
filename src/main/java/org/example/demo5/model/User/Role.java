package org.example.demo5.model.User;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("Role")
@NoArgsConstructor
@Data
public class Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String uuid;
    private String uid;
    private String role;
    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;

}
