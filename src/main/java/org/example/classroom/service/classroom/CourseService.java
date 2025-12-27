package org.example.classroom.service.classroom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.CourseMapper;
import org.example.classroom.transfer.dto.insertDto.CourseDto;
import org.example.classroom.transfer.dto.selectDto.CourseSelectDto;
import org.example.classroom.model.classroom.Course;
import org.example.classroom.util.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {
    @Resource
    CourseMapper courseMapper;
    @Resource
    ImageUtil imageUtil;


    /* ============================= 新增 ============================= */
    public Boolean insertCourse(CourseDto dto, MultipartFile file) {
        String url = imageUtil.saveImage(file);
        String courseId = UUID.randomUUID().toString();
        int number = courseMapper.refreshCountByCourseId(dto.getCourseId());
        Course course = new Course();
        course.setCourseId(courseId);
        course.setUserId(dto.getUserId());
        course.setCourseName(dto.getCourseName());
        course.setTeacherName(dto.getTeacherName());
        course.setDescription(dto.getDescription());
        course.setNeedAudit(dto.getNeedAudit());
        course.setBesides(dto.getBesides());
        course.setImageUrl(url);
        course.setNumber(dto.getNumber());   // <-- 新增
        return courseMapper.insert(course) > 0;
    }

    /* ============================= 更新 ============================= */
    public Boolean updateCourse(CourseDto dto, MultipartFile file, String oldName) {
        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setUserId(dto.getUserId());
        course.setCourseName(dto.getCourseName());
        course.setTeacherName(dto.getTeacherName());
        course.setDescription(dto.getDescription());
        course.setNeedAudit(dto.getNeedAudit());
        course.setBesides(dto.getBesides());
        course.setNumber(dto.getNumber());   // <-- 新增

        if (file != null) {
            String url = imageUtil.changeImage(file, oldName);
            course.setImageUrl(url);
        }
        return courseMapper.updateByCourseId(course);
    }

    public Boolean deleteByCourseId(String CourseId){
        return courseMapper.deleteByCourseId(CourseId);
    }

    public Boolean deleteByCourseIds(List<String> CourseIds){
        return courseMapper.deleteByCourseIds(CourseIds);
    }

    /* ============================= 分页 ============================= */
    public IPage<Course> courseIPage(CourseSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();

        Page<Course> pg = new Page<>(page, size);
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(dto.getCourseName()), Course::getCourseName, dto.getCourseName())
                .like(StringUtils.hasText(dto.getTeacherName()), Course::getTeacherName, dto.getTeacherName())
                .eq(StringUtils.hasText(dto.getCourseId()), Course::getCourseId, dto.getCourseId())
                .like(StringUtils.hasText(dto.getDescription()), Course::getDescription, dto.getDescription())
                .eq(dto.getNeedAudit() != null, Course::getNeedAudit, dto.getNeedAudit())
                .like(StringUtils.hasText(dto.getBesides()), Course::getBesides, dto.getBesides())
                .eq(dto.getNumber() != null, Course::getNumber, dto.getNumber())   // <-- 新增
                .orderByDesc(Course::getCreatedTime);

        return courseMapper.selectPage(pg, wrapper);
    }
}
