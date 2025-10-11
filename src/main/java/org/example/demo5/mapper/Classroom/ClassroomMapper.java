package org.example.demo5.mapper.Classroom;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.javassist.tools.reflect.Sample;
import org.example.demo5.dto.SampleQueryDto;
import org.example.demo5.model.Classroom.Classroom;
import org.apache.ibatis.annotations.Mapper;
import org.example.demo5.model.Classroom.ClassroomImages;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Mapper
public interface ClassroomMapper extends BaseMapper<Classroom> {
    default Classroom selectByClassroomId(String classroomId) {
        return selectOne(new QueryWrapper<Classroom>()
                .eq("classroom_id", classroomId));

    }
//查询方法，暂时不上
    default List<Classroom> findList(SampleQueryDto sampleQueryDto) {
        return this.selectList(Wrappers.<Classroom>lambdaQuery()
                //查询条件 id = #{id}
                .eq(Objects.nonNull(sampleQueryDto.getId()), Classroom::getId, sampleQueryDto.getId())
                //查询条件 name like concat('%', #{name}, '%')
                .like(StringUtils.hasText(sampleQueryDto.getName()), Classroom::getName, sampleQueryDto.getName())
                //查询条件 type = #{type}
                //.eq(StringUtils.hasText(sampleQueryDto.getType()), Sample::getType, sampleQueryDto.getType())
                //查询条件 date >= #{startDate}
                //.ge(Objects.nonNull(sampleQueryDto.getStartDate()), Sample::getDate, sampleQueryDto.getStartDate())
                //查询条件 date <= #{endDate}
                //.le(Objects.nonNull(sampleQueryDto.getEndDate()), Sample::getDate, sampleQueryDto.getEndDate())

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
