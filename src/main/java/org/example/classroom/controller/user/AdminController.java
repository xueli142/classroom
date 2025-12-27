package org.example.classroom.controller.user;


import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.insertDto.AdminDto;
import org.example.classroom.transfer.dto.insertDto.StudentDto;
import org.example.classroom.transfer.dto.insertDto.TeacherDto;
import org.example.classroom.transfer.dto.selectDto.AdminSelectDto;
import org.example.classroom.transfer.dto.selectDto.StudentSelectDto;
import org.example.classroom.transfer.dto.selectDto.TeacherSelectDto;
import org.example.classroom.transfer.dto.simpleVo.AdminVo;
import org.example.classroom.transfer.dto.simpleVo.StudentVo;
import org.example.classroom.transfer.dto.simpleVo.TeacherVo;
import org.example.classroom.service.user.AdminService;
import org.example.classroom.service.user.StudentService;

import org.example.classroom.service.user.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private StudentService studentService;


    @Resource
    private TeacherService teacherService;
    @Resource
    private AdminService adminService;

    /* 分页 + 搜索 */
    @PostMapping("/student/page")
    public ResponseDto<IPage<StudentVo>> page(@RequestBody StudentSelectDto dto) {
        return ResponseDto.success(studentService.page(dto));
    }

    /* 新增 */
    @PostMapping("/student")
    public ResponseDto<Boolean> add(@RequestBody StudentDto dto) {
        dto.setIsActive(true);
        return ResponseDto.success(studentService.insertStudent(dto));
    }

    /* 修改 */
    @PutMapping("/student")
    public ResponseDto<Boolean> update(@RequestBody StudentDto dto) {
        return ResponseDto.success(studentService.updateStudentByUserId(dto));
    }

    /* 删除 */
    @DeleteMapping("student/{userId}")
    public ResponseDto<Boolean> delstudent(@PathVariable String userId) {
        return ResponseDto.success(studentService.deleteStudentByUserId(userId));
    }
    /*批量删除*/
    @DeleteMapping("student/batch")
    public ResponseDto<Boolean> deletestudents(@RequestBody List<String> userIds){
        return ResponseDto.success(studentService.deleteBatchByUserIds(userIds));
    }
    /*批量插入*/
    @PostMapping("student/batch")
    public ResponseDto<Boolean> addBatch(@RequestBody List<StudentDto> dtoList){
        return ResponseDto.success(studentService.insertBatchStudents(dtoList));}


    @PostMapping("/admin/page")
    public ResponseDto<IPage<AdminVo>> pageAdmins(@RequestBody AdminSelectDto dto) {
        return ResponseDto.success(adminService.pageAdmins(dto));
    }

    /* 新增管理员 */
    @PostMapping("/admin")
    public ResponseDto<Boolean> addAdmin(@RequestBody AdminDto dto) {
        return ResponseDto.success(adminService.insertAdmin(dto));
    }

    /* 修改管理员 */
    @PutMapping("/admin")
    public ResponseDto<Boolean> updateAdmin(@RequestBody AdminDto dto) {
        return ResponseDto.success(adminService.updateAdminByUserId(dto));
    }

    /* 删除管理员 */
    @DeleteMapping("/admin/{userId}")
    public ResponseDto<Boolean> delAdmin(@PathVariable String userId) {
        return ResponseDto.success(adminService.deleteAdminByUserId(userId));
    }

    /* 批量删除管理员 */
    @DeleteMapping("/admin/batch")
    public ResponseDto<Boolean> deleteAdmins(@RequestBody List<String> userIds) {
        return ResponseDto.success(adminService.deleteAdminByUserIds(userIds));
    }





    /* 1. 分页 + 搜索 */
    @PostMapping("/teacher/page")
    public ResponseDto<IPage<TeacherVo>> page(@RequestBody TeacherSelectDto dto) {
        return ResponseDto.success(teacherService.page(dto));
    }

    /* 2. 新增教师 */
    @PostMapping("/teacher")
    public ResponseDto<Boolean> add(@RequestBody TeacherDto dto) {
        return ResponseDto.success(teacherService.insertTeacher(dto));
    }

    /* 3. 修改教师 */
    @PutMapping("/teacher")
    public ResponseDto<Boolean> update(@RequestBody TeacherDto dto) {
        return ResponseDto.success(teacherService.updateTeacherByUserId(dto));
    }

    /* 4. 删除教师 */
    @DeleteMapping("/teacher/{userId}")
    public ResponseDto<Boolean> delteacher(@PathVariable String userId) {
        return ResponseDto.success(teacherService.deleteTeacherByUserId(userId));
    }

    /* 5. 批量删除教师 */
    @DeleteMapping("/teacher/batch")
    public ResponseDto<Boolean> deleteteachers(@RequestBody List<String> userIds) {
        return ResponseDto.success(teacherService.deleteTeacherByUserIds(userIds));
    }
}
















































