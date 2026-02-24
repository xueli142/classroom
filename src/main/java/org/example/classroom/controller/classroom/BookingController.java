package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.selectDto.BookingSelectDto;
import org.example.classroom.model.classroom.Booking;
import org.example.classroom.service.classroom.serviceImpl.BookingServiceImpl;
import org.example.classroom.util.ResponseUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预订管理控制器
 * <p>
 * 处理教室预订的创建、更新、删除和查询操作
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Slf4j
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Resource
    private BookingServiceImpl bookingService;

    /**
     * 批量插入预订记录（整门课一次性预订）
     *
     * @param list 预订记录列表
     * @return 操作结果
     */
    @PostMapping("/insertBatch")
    public ResponseDto<Void> insertBatch(@RequestBody @Valid List<Booking> list) {
        log.info("Batch booking insert: count={}", list.size());
        bookingService.bookWholeCourse(list);
        return ResponseUtils.success();
    }

    /**
     * 单条更新预订记录
     *
     * @param booking 预订记录
     * @return 操作结果
     */
    @PutMapping("/updateById")
    public ResponseDto<Void> updateOne(@RequestBody @Valid Booking booking) {
        log.info("Update booking: bookingId={}", booking.getBookingId());
        bookingService.updateByBookingId(booking);
        return ResponseUtils.success();
    }

    /**
     * 单条删除预订记录
     *
     * @param bookingId 预订 ID
     * @return 操作结果
     */
    @DeleteMapping("/{bookingId}")
    public ResponseDto<Void> deleteOne(@PathVariable String bookingId) {
        log.info("Delete booking: bookingId={}", bookingId);
        bookingService.deleteByBookingId(bookingId);
        return ResponseUtils.success();
    }

    /**
     * 批量删除预订记录
     *
     * @param ids 预订 ID 列表
     * @return 操作结果
     */
    @DeleteMapping("/deleteByIds")
    public ResponseDto<Void> deleteBatch(@RequestBody List<String> ids) {
        log.info("Batch delete booking: count={}", ids.size());
        bookingService.deleteByBookingIds(ids);
        return ResponseUtils.success();
    }

    /**
     * 通用分页查询
     *
     * @param dto 查询条件 DTO
     * @return 分页结果
     */
    @PostMapping("/page")
    public ResponseDto<IPage<Booking>> page(@RequestBody @Valid BookingSelectDto dto) {
        log.info("Query booking page: page={}, size={}", dto.getPage(), dto.getSize());
        IPage<Booking> result = bookingService.bookingIPage(dto);
        return ResponseUtils.success(result);
    }

    /**
     * 插入单条预订记录
     *
     * @param userId      用户 ID
     * @param courseSlotId 课程时段 ID
     * @return 操作结果
     */
    @PostMapping("/insertOne")
    public ResponseDto<Void> insertOne(@RequestParam String userId,
                                       @RequestParam String courseSlotId) {
        log.info("Insert single booking: userId={}, courseSlotId={}", userId, courseSlotId);
        bookingService.insertOne(userId, courseSlotId);
        return ResponseUtils.success();
    }
}