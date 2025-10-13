package org.example.demo5.mapper.Classroom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.demo5.dto.ClassroomReserveDto;
import org.example.demo5.dto.TimeDto;
import org.example.demo5.model.Classroom.ClassroomTimeSlots;
import org.example.demo5.model.Classroom.Reservations;

import java.util.List;
import java.util.stream.Collectors;

public interface ReservationMapper extends BaseMapper<Reservations> {
    default int batchInsertReservations(List<ClassroomReserveDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return 0;
        }
        List<Reservations> list = dtoList.stream()
                .map(d -> {
                    Reservations e = new Reservations();
                    e.setClassroomId(d.getClassroomId());
                    e.setStartTime(d.getStartTime());
                    e.setEndTime(d.getEndTime());
                    e.setUid(d.getUid());
                    return e;
                })
                .collect(Collectors.toList());
        // 如果没有引入 MP 批量插插件，就用循环 insert
        int rows = 0;
        for (Reservations e : list) {
            rows += this.insert(e);
        }
        return rows;
    }











}
