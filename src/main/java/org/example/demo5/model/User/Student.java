package org.example.demo5.model.User;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student")
public class Student extends BaseUser {
    @Override public String roleName() { return "student"; }
}