package org.example.classroom.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.classroom.model.user.Teacher;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    default Boolean deleteByUserId(String userId){

        return this.delete(com.baomidou.mybatisplus.core.toolkit.Wrappers.<Teacher>lambdaQuery()
                .eq(Teacher::getUserId,userId)
        )>0;
    }
    default Boolean deleteByUserIds(java.util.List<String> userIds){

        return this.delete(com.baomidou.mybatisplus.core.toolkit.Wrappers.<Teacher>lambdaQuery()
                .in(Teacher::getUserId,userIds)
        )>0;
    }
    default Boolean updateByUserId(Teacher teacher){

        return this.update(teacher,com.baomidou.mybatisplus.core.toolkit.Wrappers.<Teacher>lambdaQuery()
                .eq(Teacher::getUserId,teacher.getUserId())
        )>0;
    }
}
