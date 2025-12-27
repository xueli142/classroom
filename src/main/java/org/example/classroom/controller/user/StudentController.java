package org.example.classroom.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.selectDto.StudentSelectDto;
import org.example.classroom.transfer.dto.simpleVo.StudentVo;
import org.example.classroom.service.user.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.classroom.transfer.dto.insertDto.StudentDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    /* 分页 + 搜索 */
    @PostMapping("/page")
    public ResponseDto<IPage<StudentVo>> page(@RequestBody StudentSelectDto dto) {
        return ResponseDto.success(studentService.page(dto));
    }

    /* 新增 */
    @PostMapping
    public ResponseDto<Boolean> add(@RequestBody StudentDto dto) {
        return ResponseDto.success(studentService.insertStudent(dto));
    }

    /* 修改 */
    @PutMapping
    public ResponseDto<Boolean> update(@RequestBody StudentDto dto) {
        return ResponseDto.success(studentService.updateStudentByUserId(dto));
    }

    /* 删除 */
    @DeleteMapping("/{userId}")
    public ResponseDto<Boolean> del(@PathVariable String userId) {
        return ResponseDto.success(studentService.deleteStudentByUserId(userId));
    }
    /*批量删除*/
    @DeleteMapping("/batch")
    public ResponseDto<Boolean> deletes(@RequestBody List<String> userIds){
        return ResponseDto.success(studentService.deleteBatchByUserIds(userIds));
    }
    /*批量插入*/
    @PostMapping("/batch")
    public ResponseDto<Boolean> addBatch(@RequestBody List<StudentDto> dtoList){
        return ResponseDto.success(studentService.insertBatchStudents(dtoList));}




}