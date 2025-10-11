package org.example.demo5.mapper.User;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.demo5.model.User.Admin;

@Mapper
public interface AdminMapper extends BaseUserMapper<Admin> {}