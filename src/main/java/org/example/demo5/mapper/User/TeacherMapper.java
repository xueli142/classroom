package org.example.demo5.mapper.User;

import org.apache.ibatis.annotations.Mapper;
import org.example.demo5.model.User.Teacher;

@Mapper
public interface TeacherMapper extends BaseUserMapper<Teacher> {}