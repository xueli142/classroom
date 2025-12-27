package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.insertDto.ClassroomDto;
import org.example.classroom.transfer.dto.selectDto.ClassroomSelectDto;
import org.example.classroom.model.classroom.Classroom;
import org.example.classroom.service.classroom.ClassroomService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/classroom")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Boolean> insertClassroom(
            @RequestPart("dto") ClassroomDto dto,   // 对应前端 FormData 里的 dto
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseDto.success(classroomService.insertClassroom(dto, file));
    }


    @DeleteMapping("/{classroomId}")
    public ResponseDto<Boolean> deleteByClassroomId(@PathVariable String classroomId) {
        return ResponseDto.success(classroomService.deleteByClassroomId(classroomId));
    }

    @DeleteMapping("/deleteByIds")
    public ResponseDto<Boolean> deleteByClassroomIds(@RequestBody List<String> classroomIds) {
        return ResponseDto.success(classroomService.deleteByClassroomIds(classroomIds));
    }

    @PostMapping("/page")
    public ResponseDto<IPage<Classroom>> classroomIPage(@RequestBody ClassroomSelectDto dto) {
        return ResponseDto.success(classroomService.classroomIPage(dto));
    }
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Boolean> updateByCourseId(@RequestPart("dto") ClassroomDto dto,
                                                 @RequestPart(value = "file", required = false) MultipartFile file,
                                                 @RequestParam("oldName") String oldName){
        return ResponseDto.success(classroomService.updateClassroom(dto, file, oldName));
    }


}