package org.example.classroom.controller.besides;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.insertDto.EventDto;
import org.example.classroom.transfer.dto.selectDto.EventSelectDto;
import org.example.classroom.model.besides.Event;
import org.example.classroom.service.besides.EventService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * 新增活动（支持封面图上传）
     */
    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Boolean> insertEvent(@RequestPart("dto") EventDto dto,
                                            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseDto.success(eventService.insertEvent(dto, file));
    }

    /**
     * 更新活动（支持封面图替换）
     */
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Boolean> updateEvent(@RequestPart("dto") EventDto dto,
                                            @RequestPart(value = "file", required = false) MultipartFile file,
                                            @RequestParam("oldName") String oldName) {
        return ResponseDto.success(eventService.updateEvent(dto, file, oldName));
    }

    /**
     * 根据主键删除
     */
    @DeleteMapping("/{eventId}")
    public ResponseDto<Boolean> deleteByEventId(@PathVariable String eventId) {
        return ResponseDto.success(eventService.deleteByEventId(eventId));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/deleteByIds")
    public ResponseDto<Boolean> deleteByEventIds(@RequestBody List<String> eventIds) {
        return ResponseDto.success(eventService.deleteByEventIds(eventIds));
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public ResponseDto<IPage<Event>> eventIPage(@RequestBody EventSelectDto dto) {
        return ResponseDto.success(eventService.eventIPage(dto));
    }
}