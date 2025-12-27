package org.example.classroom.dao.classroom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.classroom.model.classroom.Classroom;
import org.example.classroom.model.classroom.Course;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    default Boolean deleteByCourseId(String courseId) {
        return delete(Wrappers.<Course>lambdaQuery()
                .eq(Course::getCourseId, courseId)) > 0;
    }

    default Boolean deleteByCourseIds(List<String> courseIds) {
        return delete(Wrappers.<Course>lambdaQuery()
                .in(Course::getCourseId, courseIds)) > 0;
    }

    default Boolean updateByCourseId(Course course) {
        return update(course, Wrappers.<Course>lambdaQuery()
                .eq(Course::getCourseId, course.getCourseId())) > 0;
    }


    @Update("UPDATE course " +
            "SET number = (SELECT COUNT(*) " +
            "                    FROM student_course s " +
            "                    WHERE s.course_id = course.course_id) " +
            "WHERE course_id = #{courseId}")
    //s.deleted表示这个字段还没有删除
    int refreshCountByCourseId(@Param("courseId") String courseId);

}
