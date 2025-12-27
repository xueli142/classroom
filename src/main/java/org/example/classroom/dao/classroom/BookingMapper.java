package org.example.classroom.dao.classroom;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import org.example.classroom.model.classroom.Booking;
import org.example.classroom.model.classroom.StudentCourse;

import java.util.List;
import java.util.WeakHashMap;

public interface BookingMapper extends BaseMapper<Booking> {
    default Boolean  deleteByBookingId(String bookingId){
        return delete(Wrappers.<Booking>lambdaQuery()
                .eq(Booking::getBookingId,bookingId))>0;

    }
    default Boolean deleteByCourseId(String id){
        return delete(Wrappers.<Booking>lambdaQuery()
                .eq(Booking::getBookingId,id))>0;
    }
    /**
     * 根据课程 ID 批量删除预约记录
     */
    default int deleteByCourseIds(List<String> courseIds) {
        return delete(Wrappers.<Booking>lambdaQuery()
                .in(Booking::getEventId, courseIds));
    }
    default Boolean updateByBookingId(Booking booking){

        return update(booking,Wrappers.<Booking>lambdaUpdate()
                .eq(Booking::getBookingId,booking.getBookingId()))>0;

    }

    default Boolean deleteByBookingId(List<String> ids){
        return delete(Wrappers.<Booking>lambdaQuery()
                .in(Booking::getBookingId,ids))>0;
    }
    default List<String>selectSlotIdsByUserId(String id){
            return selectObjs(
                    new QueryWrapper<Booking>()
                            .select("course_slot_id")
                            .eq("user_id",id)
            ).stream()
                    .map(String::valueOf)
                    .toList();
    }


}
