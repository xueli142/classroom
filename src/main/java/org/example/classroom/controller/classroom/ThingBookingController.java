package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.classroom.model.classroom.ThingBooking;
import org.example.classroom.service.classroom.service.ThingBookingService;
import org.example.classroom.service.classroom.serviceImpl.ThingBookingServiceImpl;
import org.example.classroom.transfer.dto.insertDto.ThingBookingDto;
import org.example.classroom.transfer.dto.selectDto.ThingBookingSelectDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thing-booking")
@RequiredArgsConstructor
public class ThingBookingController {

    private final ThingBookingServiceImpl bookingService;

      /* ------------- 查询 ------------- */
    @PostMapping("/page")
    public IPage<ThingBooking> page(@RequestBody ThingBookingSelectDto dto) {
        return bookingService.pageByDto(dto);
    }

    @GetMapping("/{id}")
    public ThingBooking one(@PathVariable String id) {
        return bookingService.getById(id);
    }

    /* ------------- 新增 ------------- */
    @PostMapping
    public Boolean save(@RequestBody ThingBookingDto dto) {
        return bookingService.insertOne(dto);
    }

    @PostMapping("/batch")
    public Boolean batch(@RequestBody List<ThingBookingDto> dtoList) {
        return bookingService.insertBatch(dtoList);
    }

    /* ------------- 修改 ------------- */
    @PutMapping
    public Boolean update(@RequestBody ThingBooking entity) {
        return bookingService.updateById(entity);
    }

    /* ------------- 删除 ------------- */
    @DeleteMapping("/{id}")
    public Boolean del(@PathVariable String id) {
        return bookingService.removeById(id);
    }

    @DeleteMapping
    public Boolean delBatch(@RequestBody List<String> ids) {
        return bookingService.removeByIds(ids);
    }
}