package org.example.classroom.util;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.classroom.dao.classroom.CourseMapper;
import org.example.classroom.model.classroom.StudentCourse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component


public class StudentCourseRefreshAspect {

    @Resource
    private CourseMapper courseMapper;

    /* 插入后 */
    @AfterReturning("execution(* org.example.classroom.dao.classroom.StudentCourseMapper.insert(..)) && args(entity)")
    public void afterInsert(JoinPoint jp, StudentCourse entity){
        courseMapper.refreshCountByCourseId(entity.getCourseId());
    }
    /* 批量插入后 */
    @AfterReturning("execution(* org.example.classroom.service.classroom.StudentCourseService.insertStudentCourseBatch(..)) && args(list)")
    public void afterInsertBatch(JoinPoint jp, List<StudentCourse> list) {
        Set<String> courseIds = list.stream()
                .map(StudentCourse::getCourseId)
                .collect(Collectors.toSet());
        courseIds.forEach(courseMapper::refreshCountByCourseId);
    }

    /* 更新后（含软删） */
    @AfterReturning("execution(* org.example.classroom.dao.classroom.StudentCourseMapper.updateById(..)) && args(entity)")
    public void afterUpdate(JoinPoint jp, StudentCourse entity){
        courseMapper.refreshCountByCourseId(entity.getCourseId());
    }
}