package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.selectDto.BookingSelectDto;
import org.example.classroom.model.classroom.Booking;
import org.example.classroom.service.classroom.serviceImpl.BookingServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预订管理控制器
 * 严格按 BookingServiceImpl 已有方法暴露
 */
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Resource
    private BookingServiceImpl bookingService;

    /* -----------------------------------------------------------
       批量插入（整门课一次性预订）
       ----------------------------------------------------------- */
    @PostMapping("/insertBatch")
    public ResponseDto<Boolean> insertBatch(@RequestBody List<Booking> list) {
        return ResponseDto.success(bookingService.bookWholeCourse(list));
    }

    /* -----------------------------------------------------------
       单条更新（by bookingId）
       ----------------------------------------------------------- */
    @PutMapping("/updateById")
    public ResponseDto<Boolean> updateOne(@RequestBody Booking booking) {
        return ResponseDto.success(bookingService.updateByBookingId(booking));
    }

    /* -----------------------------------------------------------
       单条删除（by bookingId）
       ----------------------------------------------------------- */
    @DeleteMapping("/{bookingId}")
    public ResponseDto<Boolean> deleteOne(@PathVariable String bookingId) {
        return ResponseDto.success(bookingService.deleteByBookingId(bookingId));
    }

    /* -----------------------------------------------------------
       批量删除（by bookingIds）
       ----------------------------------------------------------- */
    @DeleteMapping("/deleteByIds")
    public ResponseDto<Boolean> deleteBatch(@RequestBody List<String> ids) {
        return ResponseDto.success(bookingService.deleteByBookingIds(ids));
    }

    /* -----------------------------------------------------------
       通用分页查询
       ----------------------------------------------------------- */
    @PostMapping("/page")
    public ResponseDto<IPage<Booking>> page(@RequestBody BookingSelectDto dto) {
        return ResponseDto.success(bookingService.bookingIPage(dto));
    }
    @PostMapping("/insertOne")
    public ResponseDto<Boolean> insertOne(@RequestParam String userId,
                                          @RequestParam String courseSlotId){
        return ResponseDto.success(bookingService.insertOne(userId,courseSlotId));

    }

}