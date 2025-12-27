package org.example.classroom.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.CourseMapper;
import org.example.classroom.model.classroom.Course;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseCountSyncJob {

    @Resource
    private CourseMapper courseMapper;

    @Scheduled(cron = "0 0/10 * * * ?")   // 每 10 分钟跑一次
    public void syncAll() {
        List<String> ids = courseMapper.selectObjs(
                new QueryWrapper<Course>().select("course_id"));
        ids.forEach(courseMapper::refreshCountByCourseId);
    }
}