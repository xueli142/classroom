package org.example.demo5.mapper.Classroom;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.demo5.dto.SelectClassroomDto;
import org.example.demo5.model.Classroom.Classroom;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Mapper
public interface ClassroomMapper extends BaseMapper<Classroom> {
    default Classroom selectByClassroomId(String classroomId) {
        return selectOne(new QueryWrapper<Classroom>()
                .eq("classroom_id", classroomId));

    }
//查询方法
default List<Classroom> findList(SelectClassroomDto q) {
    return this.selectList(
            Wrappers.<Classroom>lambdaQuery()
                    // id = #{id}
                    .eq(Objects.nonNull(q.getId()), Classroom::getId, q.getId())
                    // name like concat('%', #{name}, '%')
                    .like(StringUtils.hasText(q.getName()), Classroom::getName, q.getName())
                    // classroom_id = #{classroomId}
                    .eq(StringUtils.hasText(q.getClassroomId()), Classroom::getClassroomId, q.getClassroomId())
                    // location like concat('%', #{location}, '%')
                    .like(StringUtils.hasText(q.getLocation()), Classroom::getLocation, q.getLocation())
                    // is_active = #{isActive}
                    .eq(Objects.nonNull(q.getIsActive()), Classroom::getIsActive, q.getIsActive())
                    // number = #{number}
                    .eq(Objects.nonNull(q.getNumber()), Classroom::getNumber, q.getNumber())
                    // 排序（可选）
                    .orderByAsc(Classroom::getId)
    );
}
    default boolean deleteByClassroomId(String classroomId) {
        return this.delete(new QueryWrapper<Classroom>()
                .eq("classroom_id", classroomId)) > 0;
    }

    default boolean updatedByClassroom(Classroom classroom) {
        return this.update(classroom, new QueryWrapper<Classroom>()
                .eq("classroom_id", classroom.getClassroomId())) > 0;
    }
}
