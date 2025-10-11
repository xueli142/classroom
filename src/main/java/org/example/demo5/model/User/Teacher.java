package org.example.demo5.model.User;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher")
public class Teacher extends BaseUser {
    @Override public String roleName() { return "teacher"; }
}