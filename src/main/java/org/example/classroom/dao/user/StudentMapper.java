package org.example.classroom.dao.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.example.classroom.model.user.Student;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    default boolean deleteByUserId(String userId) {
        return delete(new LambdaQueryWrapper<Student>()
                .eq(Student::getUserId, userId)) > 0;
    }

    default  boolean updateByUserId(Student student) {
        return update(student, new LambdaQueryWrapper<Student>()
                .eq(Student::getUserId, student.getUserId())) > 0;
    }

    default boolean deleteByUserIds(List<String> userIds) {
        return delete(Wrappers.<Student>lambdaQuery()
                .in(Student::getUserId, userIds))>0;
    }



}
