package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.insertDto.CourseDto;
import org.example.classroom.transfer.dto.selectDto.CourseSelectDto;
import org.example.classroom.model.classroom.Course;
import org.example.classroom.service.classroom.CourseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * 新增课程（支持封面图上传）
     */
    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Boolean> insertCourse(@RequestPart("dto") CourseDto dto,
                                             @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseDto.success(courseService.insertCourse(dto, file));
    }

    /**
     * 更新课程（支持封面图替换）
     */
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Boolean> updateCourse(@RequestPart("dto") CourseDto dto,
                                             @RequestPart(value = "file", required = false) MultipartFile file,
                                             @RequestParam("oldName") String oldName) {
        return ResponseDto.success(courseService.updateCourse(dto, file, oldName));
    }

    /**
     * 根据主键删除
     */
    @DeleteMapping("/{courseId}")
    public ResponseDto<Boolean> deleteByCourseId(@PathVariable String courseId) {
        return ResponseDto.success(courseService.deleteByCourseId(courseId));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/deleteByIds")
    public ResponseDto<Boolean> deleteByCourseIds(@RequestBody List<String> courseIds) {
        return ResponseDto.success(courseService.deleteByCourseIds(courseIds));
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public ResponseDto<IPage<Course>> courseIPage(@RequestBody CourseSelectDto dto) {
        return ResponseDto.success(courseService.courseIPage(dto));
    }


}