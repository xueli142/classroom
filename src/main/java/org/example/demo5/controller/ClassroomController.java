package org.example.demo5.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.demo5.dto.ClassroomDto;
import org.example.demo5.dto.ResponseDto;
import org.example.demo5.mapper.Classroom.ClassroomImagesMapper;
import org.example.demo5.mapper.Classroom.ClassroomMapper;
import org.example.demo5.model.Classroom.Classroom;
import org.example.demo5.model.Classroom.ClassroomImages;
import org.example.demo5.service.Classroom.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClassroomController {
    @Autowired
    private  ClassroomService classroomService;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private ClassroomImagesMapper classroomImagesMapper;


    /** 1.创建教室 */
    @PostMapping("/classroom")
    public ResponseDto createClassroom(@RequestBody ClassroomDto dto) {
       return   ResponseDto.success(classroomService.addClassroom(dto));

    }

    /** 2. 上传图片 */
    @PostMapping("/classroom/{classroomId}/image")
    public ResponseDto uploadImage(@PathVariable String classroomId,
                              @RequestParam("file") MultipartFile file) {
        return ResponseDto.success(classroomService.saveImage(classroomId, file));
    }
    /** 3. 分页获取教室列表 */
    @GetMapping("/classroom/page")
    public ResponseDto<Map<String,Object>> pageClassrooms(@RequestParam(defaultValue = "0") int page) {
        List<Classroom> list = classroomService.pageByIdAsc(page);
        List<String> urllist = classroomService.pageByUrl(list);
        Map<String,Object>vo = Map.of(
                "classrooms", list,
                "urls",      urllist
        );
        return ResponseDto.success(vo);   // 原封不动把 Service 给的列表甩出去

    }
    /**4. 单独查询,这个暂时没有写完，是模糊查询**/
    @GetMapping("/classroom/{classroomId}" )
    public ResponseDto selectClassroom(@PathVariable String classroomId) {

        return classroomService.selectClassroom(classroomId);
    }
    /**5. 删除教室**/
    @DeleteMapping("/classroom/{classroomId}")
    public ResponseDto deleteClassroom(@PathVariable String classroomId) {
        boolean result1 = classroomImagesMapper.deleteByClassroomId(classroomId);
        boolean result2 =  classroomMapper.deleteByClassroomId(classroomId);

        return result2 ? ResponseDto.success("删除成功") : ResponseDto.error("删除失败");
    }
    /**6. 更新教室**/
    @PutMapping("/classroom/{classroomId}")
    public ResponseDto updateClassroom(@RequestBody Classroom classroom){

        boolean result2 = classroomMapper.updatedByClassroom(classroom);
        return result2 ? ResponseDto.success("更新成功") : ResponseDto.error("更新失败");
    }
    /**7.预约教室**/
    @PostMapping("/classroom/reserve")
    public ResponseDto reserveClassroom(@RequestBody Classroom classroom){
        boolean result = classroomMapper.updatedByClassroom(classroom);
        return result ? ResponseDto.success("预约成功") : ResponseDto.error("预约失败");
    }
    /**8.单独修改图片**/
    @PutMapping("/classroom/{classroomId}/image")
    public ResponseDto updateImage(@PathVariable String classroomId,
                              @RequestParam("file") MultipartFile file){
        return ResponseDto.success(classroomService.saveImage(classroomId, file));
    }


}