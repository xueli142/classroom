    package org.example.classroom.controller.classroom;

    import com.baomidou.mybatisplus.core.metadata.IPage;
    import jakarta.annotation.Resource;
    import org.example.classroom.transfer.dto.besides.ResponseDto;
    import org.example.classroom.transfer.dto.insertDto.CourseSlotDto;
    import org.example.classroom.transfer.dto.selectDto.CourseSlotSelectDto;
    import org.example.classroom.model.classroom.CourseSlot;
    import org.example.classroom.service.classroom.serviceImpl.CourseSlotServiceImpl;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/course-slot")
    public class CourseSlotController {

        @Resource
        private CourseSlotServiceImpl courseSlotService;
        @PutMapping("/updateById")
        public ResponseDto<Boolean>updateOne(@RequestBody CourseSlotDto dto){

            return ResponseDto.success(courseSlotService.updateCourseSlot(dto));
        }
        @PutMapping("/updateByIds")
        public ResponseDto<Boolean>updateBatch(@RequestBody List<CourseSlotDto> dtoList){
            return ResponseDto.success(courseSlotService.updateCourseSlots(dtoList));
        }
        /* ===========================================================
           单条删除
           =========================================================== */
        @DeleteMapping("/{courseSlotId}")
        public ResponseDto<Boolean> deleteOne(@PathVariable String courseSlotId) {
            return ResponseDto.success(courseSlotService.deleteCourseSlotById(courseSlotId));
        }

        /* ===========================================================
           批量删除
           =========================================================== */
        @DeleteMapping("/deleteByIds")
        public ResponseDto<Boolean> deleteBatch(@RequestBody List<String> courseSlotIds) {
            return ResponseDto.success(courseSlotService.deleteCourseSlotByIds(courseSlotIds));
        }

        /* ===========================================================
           批量插入（一张公共图）
           =========================================================== */
        @PostMapping("/insertBatch")
        public ResponseDto<Boolean> insertBatch(@RequestBody List<CourseSlotDto> dtoList) {
            return ResponseDto.success(courseSlotService.insertBatchCourseSlots(dtoList));
        }

        /* ===========================================================
           通用分页（管理后台、任意条件）
           =========================================================== */
        @PostMapping("/page")
        public ResponseDto<IPage<CourseSlot>> page(@RequestBody CourseSlotSelectDto dto) {
            return ResponseDto.success(courseSlotService.slotPage(dto));
        }



        /* ===========================================================
           老师：看我本周时段
           =========================================================== */
        @PostMapping("/teacherSlotPage")
        public ResponseDto<List<CourseSlot>> teacherSlotPage(@RequestBody CourseSlotSelectDto dto) {
            return ResponseDto.success(courseSlotService.teacherSlotPage(dto));
        }

        /* ===========================================================
           教室：占用明细
           =========================================================== */
        @PostMapping("/classroomOccupyPage")
        public ResponseDto<List<CourseSlot>> classroomOccupyPage(@RequestBody CourseSlotSelectDto dto) {
            return ResponseDto.success(courseSlotService.classroomOccupyPage(dto));
        }
        @PostMapping("/studentSlotPage")
        public ResponseDto<List<CourseSlot>> studentOccupyPage(@RequestBody CourseSlotSelectDto dto) {
            return ResponseDto.success(courseSlotService.studentOccupyPage(dto));
        }

     //个人课表查询
        @PostMapping("/userSlotPage")
        public ResponseDto<List<CourseSlot>> userSlotPage(@RequestBody CourseSlotSelectDto dto){
        return ResponseDto.success(courseSlotService.userSlotPage(dto));
        }

        @PostMapping("/selectList")
        public ResponseDto<List<CourseSlot>> listByDto(@RequestBody CourseSlotSelectDto dto){
            return ResponseDto.success(courseSlotService.listByDto(dto));
        }
    }