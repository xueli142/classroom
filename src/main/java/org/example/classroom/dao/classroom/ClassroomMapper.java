package org.example.classroom.dao.classroom;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.classroom.model.classroom.Classroom;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ClassroomMapper extends BaseMapper<Classroom> {



    default Boolean deleteByClassroomId(String classroomId){


        return delete(Wrappers.<Classroom>lambdaQuery()
                .eq(Classroom::getClassroomId,classroomId))>0;

    }

    default Boolean deleteByClassroomIds(List<String> classroomIds){
        return  delete(Wrappers.<Classroom>lambdaQuery()
                .in(Classroom::getClassroomId,classroomIds))>0;

    }


    default Boolean updateByClassroomId(Classroom classroom){
        return update(classroom,Wrappers.<Classroom>lambdaUpdate()
                .eq(Classroom::getClassroomId,classroom.getClassroomId()))>0;

    }

    default String selectUrlByClassroomId(String classroomId) {
        return selectOne(Wrappers.<Classroom>lambdaQuery()
                .eq(Classroom::getClassroomId, classroomId)
        ).getImageUrl();
    }

    default List<String> selectUrlByClassroomIds(List<String> classroomIds) {
        // 使用 selectList 查询 Classroom 对象列表
        List<Classroom> classrooms = selectList(
                Wrappers.<Classroom>lambdaQuery()
                        .in(Classroom::getClassroomId, classroomIds)
                        .select(Classroom::getImageUrl) // 只查询 imageUrl 字段
        );
        // 提取 imageUrl 字段值到 List<String>
        return classrooms.stream()
                .map(Classroom::getImageUrl)
                .collect(Collectors.toList());
    }

    @Select("SELECT * FROM classroom ${ew.customSqlSegment}")
    IPage<Classroom> selectPageByWrapper(Page<?> page, @Param("ew") Wrapper<?> queryWrapper);
}


