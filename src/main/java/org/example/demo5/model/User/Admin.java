package org.example.demo5.model.User;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("admin")
public class Admin extends BaseUser {
    @Override public String roleName() { return "admin"; }
}