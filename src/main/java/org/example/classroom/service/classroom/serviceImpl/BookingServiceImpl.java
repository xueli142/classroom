package org.example.classroom.service.classroom.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.BookingMapper;
import org.example.classroom.dao.classroom.CourseSlotMapper;
import org.example.classroom.dao.classroom.StudentCourseMapper;
import org.example.classroom.service.classroom.service.BookingService;
import org.example.classroom.transfer.dto.selectDto.BookingSelectDto;
import org.example.classroom.model.classroom.Booking;
import org.example.classroom.model.classroom.CourseSlot;
import org.example.classroom.model.classroom.StudentCourse;
import org.example.classroom.util.ToolStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class BookingServiceImpl
        extends ServiceImpl<BookingMapper, Booking>    implements BookingService  // ← 实例来源
{                   // 可选额外接口

    @Resource
    private BookingMapper bookingMapper;
    @Resource
    private StudentCourseMapper studentCourseMapper;
    @Resource
    private CourseSlotMapper courseSlotMapper;
    @Transactional(rollbackFor = Exception.class)
    public Boolean  bookWholeCourse(List<Booking> dtos) {
        List<Booking> list = dtos.stream()
                .map(dto -> {
                    Booking b = new Booking();
                    b.setUserId(dto.getUserId());
                    b.setBookingId(UUID.randomUUID().toString());
                    b.setEventId(dto.getEventId());
                    b.setClassroomId(dto.getClassroomId());
                    b.setBookingById(dto.getBookingById());
                    b.setStatus(ToolStatus.PUBLISHED.toString());
                    b.setCourseSlotId(dto.getCourseSlotId());
                    return b;
                })
                .collect(Collectors.toList());

        // 每 1000 条拼一条 SQL，一次性发过去
        return  this.saveBatch(list, 1000);
    }

    //因为事件新增人员
    public Boolean bookinginsert(Booking book){
        Booking booking = new Booking();
            booking.setBookingId(UUID.randomUUID().toString());
        return bookingMapper.insert(booking)>0;

    }




    @Transactional(rollbackFor = Exception.class)
    public Boolean insertAllBooking(List<CourseSlot> dtoList){
        if (dtoList== null || dtoList.isEmpty()) {
            System.out.println("没有新增的课程槽位，跳过 booking 插入");
            return true;
        }

        CourseSlot firstSlot = dtoList.get(0);
        String courseId      = firstSlot.getCourseId();
        String classroomId   = firstSlot.getClassroomId();
        //这里的teacherId 对应bookingBy ,即活动的创建人
        String teacherId     = firstSlot.getTeacherId();
        List<StudentCourse> students=studentCourseMapper.selectByCourseId(courseId);



        List<String> userIds = students.stream()
                .map(StudentCourse::getUserId)
                .toList();

        if (CollUtil.isEmpty(students)) {
            return true;          // 没学生直接返回，按业务可自行调整
        }

        Booking template = new Booking();
        template.setBookingId(UUID.randomUUID().toString());
        template.setClassroomId(classroomId);
        template.setEventId(courseId);
        template.setBookingById(teacherId);
        template.setStatus(ToolStatus.PUBLISHED.toString());


        List<Booking> bookingList = dtoList.stream()
                .flatMap(dto -> userIds.stream().map(uid -> {
                    Booking b = new Booking();
                    BeanUtil.copyProperties(template, b);
                    b.setBookingId(UUID.randomUUID().toString());
                    b.setCourseSlotId(dto.getCourseSlotId());
                    b.setUserId(uid);
                    return b;
                }))
                .toList();



        return this.saveBatch(bookingList,1000);
    }




    public Boolean updateByBookingId(Booking booking){
        return bookingMapper.updateByBookingId(booking);

    }

    public Boolean deleteByBookingId(String bookId){
        return bookingMapper.deleteByBookingId(bookId);
    }

    public Boolean deleteByBookingIds(List<String> ids){
        return bookingMapper.deleteByBookingId(ids);
    }


    public IPage<Booking>bookingIPage(BookingSelectDto dto){
        long page =dto.getPage()==null?1: dto.getPage();
        long size =dto.getSize()==null?10:dto.getSize();
        Page<Booking> pg=new Page<>(page,size);
        LambdaQueryWrapper<Booking>wrapper=new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(dto.getUserId()),      Booking::getUserId,      dto.getUserId());
        wrapper.eq(StringUtils.hasText(dto.getBookingId()),   Booking::getBookingId,   dto.getBookingId());
        wrapper.eq(StringUtils.hasText(dto.getEventId()),     Booking::getEventId,     dto.getEventId());
        wrapper.eq(StringUtils.hasText(dto.getBookingById()), Booking::getBookingById, dto.getBookingById());
        wrapper.eq(StringUtils.hasText(dto.getClassroomId()), Booking::getClassroomId, dto.getClassroomId());
        wrapper.eq(StringUtils.hasText(dto.getCourseSlotId()),Booking::getCourseSlotId,dto.getBookingById());
        // 2. Boolean 字段：不为 null 才 eq
        wrapper.eq(dto.getStatus() != null, Booking::getStatus, dto.getStatus());

        return bookingMapper.selectPage(pg,wrapper);
    }

    public Boolean insertOne(String userId,String courseSlotId){
        Booking booking = new Booking();
              CourseSlot courseSlot=  courseSlotMapper.selectOneBySlotId(courseSlotId);

        booking.setUserId(userId);                              // 当前用户
        booking.setBookingId(UUID.randomUUID().toString());     // 主键
        booking.setEventId(courseSlot.getCourseId());            // 从排课里取
        booking.setClassroomId(courseSlot.getClassroomId());    // 从排课里取
        booking.setBookingById(userId);                         // 谁订的就是谁
        booking.setStatus(ToolStatus.PUBLISHED.toString());     // 固定状态
        booking.setCourseSlotId(courseSlotId);


        return bookingMapper.insert(booking)>0;
    }




}