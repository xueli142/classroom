package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.selectDto.StudentCourseSelectDto;
import org.example.classroom.model.classroom.StudentCourse;
import org.example.classroom.service.classroom.StudentCourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-course")
public class StudentCourseController {

        @Resource
     StudentCourseService studentCourseService;



    /* -------------------- 增 -------------------- */
    @PostMapping("/insert")
    public ResponseDto<Boolean> insert(@RequestBody StudentCourse studentCourse) {
        return ResponseDto.success(studentCourseService.insertStudentCourse(studentCourse));
    }

    @PostMapping("/insertBatch")
    public ResponseDto<Boolean> insertBatch(@RequestBody List<StudentCourse> list) {
        return ResponseDto.success(studentCourseService.insertStudentCourseBatch(list));
    }

    /* -------------------- 改 -------------------- */
    @PutMapping("/update")
    public ResponseDto<Boolean> update(@RequestBody StudentCourse studentCourse) {
        return ResponseDto.success(studentCourseService.updateStudentCourse(studentCourse));
    }

    /* -------------------- 单条删 -------------------- */
    @DeleteMapping("/by-user/{userId}")
    public ResponseDto<Boolean> deleteByUserId(@PathVariable String userId) {
        return ResponseDto.success(studentCourseService.deleteByUserId(userId));
    }

    @DeleteMapping("/by-course/{courseId}")
    public ResponseDto<Boolean> deleteByCourseId(@PathVariable String courseId) {
        return ResponseDto.success(studentCourseService.deleteByCourseId(courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Boolean> deleteById(@PathVariable String id) {
        return ResponseDto.success(studentCourseService.deleteByStudentCourseId(id));
    }

    /* -------------------- 批量删 -------------------- */
    @DeleteMapping("/deleteByUserIds")
    public ResponseDto<Boolean> deleteByUserIds(@RequestBody List<String> userIds) {
        return ResponseDto.success(studentCourseService.deleteBatchByUserIds(userIds));
    }

    @DeleteMapping("/deleteByCourseIds")
    public ResponseDto<Boolean> deleteByCourseIds(@RequestBody List<String> courseIds) {
        return ResponseDto.success(studentCourseService.deleteBatchByCourseIds(courseIds));
    }

    /* -------------------- 分页查 -------------------- */
    @PostMapping("/page")
    public ResponseDto<IPage<StudentCourse>> page(@RequestBody StudentCourseSelectDto dto) {
        return ResponseDto.success(studentCourseService.studentCoursePage(dto));
    }
    @PostMapping("/insertOne")
    public ResponseDto<Boolean> insertOne(@RequestParam String userId,
                                          @RequestParam String courseId){
        return ResponseDto.success(studentCourseService.insertOne(userId,courseId));
    }
}