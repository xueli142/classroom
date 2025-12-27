package org.example.classroom.dao.classroom;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.example.classroom.model.classroom.StudentCourse;

import java.util.List;

@Mapper
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {
    default Boolean deleteByUserId(String id){
        return delete(Wrappers.<StudentCourse>lambdaQuery()
                .eq(StudentCourse::getUserId,id))>0;
    }
    default Boolean deleteByCourseId(String id){
        return delete(Wrappers.<StudentCourse>lambdaQuery()
                .eq(StudentCourse::getCourseId,id))>0;
    }
    default Boolean deleteByStudentCourseId(String id){
        return delete(Wrappers.<StudentCourse>lambdaQuery()
                .eq(StudentCourse::getStudentCourseId,id))>0;
    }
    /**
     * 根据学生 ID 列表批量删除选课记录
     */
    default Boolean deleteBatchByUserIds(List<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return true;
        }
        return delete(Wrappers.<StudentCourse>lambdaQuery()
                .in(StudentCourse::getUserId, userIds)) == userIds.size();
    }



    /**
     * 根据主键 ID 列表批量删除选课记录
     */
    default Boolean deleteBatchByStudentCourseIds(List<String> studentCourseIds) {
        if (CollUtil.isEmpty(studentCourseIds)) {
            return true;
        }
        return delete(Wrappers.<StudentCourse>lambdaQuery()
                .in(StudentCourse::getStudentCourseId, studentCourseIds)) == studentCourseIds.size();
    }

    default Boolean updateByStudentCourseId(StudentCourse studentCourse){
        return update(Wrappers.<StudentCourse>lambdaQuery()
                .eq(StudentCourse::getStudentCourseId,studentCourse.getStudentCourseId()))>0;
    }

    default  List<StudentCourse> selectByCourseId(String courseId){
        return selectList(Wrappers.<StudentCourse>lambdaQuery()
                .eq(StudentCourse::getCourseId,courseId));
    }








































































}
