package org.example.classroom.dao.classroom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.classroom.model.classroom.ThingBooking;

import java.util.List;

public interface ThingBookingMapper extends BaseMapper<ThingBooking> {

   default Boolean deleteByThingId(String id){

       return delete(Wrappers.<ThingBooking>lambdaQuery()
               .eq(ThingBooking::getThingBookingId,id))>0;
   }

   default Boolean deleteByThingIds(List<String> ids){
       return delete(Wrappers.<ThingBooking>lambdaQuery()
               .in(ThingBooking::getThingBookingId))>0;
   }

   default  Boolean updateByThingId(ThingBooking thing){
       return update(thing ,Wrappers.<ThingBooking>lambdaUpdate()
               .eq(ThingBooking::getThingBookingId,thing.getThingBookingId()))>0;
   }




}
