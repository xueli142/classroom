package org.example.demo5.model.User;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public abstract class BaseUser {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private String uid;
    private String  uuid;
    private String  name;
    private String  password;
    private String  email;
    private String  phone;
    private String role;

    @TableField(fill = FieldFill.INSERT) // 插入时填充
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
    private LocalDateTime updatedTime;


    //强制每个子类（student / teacher / admin）
    // “自报家门”，告诉程序 “我这张表对应的角色字符串是什么”，后面注册时直接拿这个值写 role 表，杜绝硬编码。
    public abstract String roleName();

}

