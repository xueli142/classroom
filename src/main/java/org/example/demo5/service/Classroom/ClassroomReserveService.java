package org.example.demo5.service.Classroom;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.demo5.dto.ClassroomReserveDto;
import org.example.demo5.mapper.Classroom.ReservationMapper;
import org.example.demo5.model.Classroom.Reservations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ClassroomReserveService extends ServiceImpl<ReservationMapper, Reservations> {

    /* ===================== 1. 批量新增（单条/多条） ===================== */
    public int batchInsertReservations(List<ClassroomReserveDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {   // 原生写法
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

        boolean success = saveBatch(list);   // MP 批量插
        return success ? list.size() : 0;
    }

    /* ===================== 2. 批量全量更新（单条/多条） ===================== */
    public int batchUpdateReservations(List<ClassroomReserveDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return 0;
        }
        List<Reservations> list = dtoList.stream()
                .map(d -> {
                    Reservations e = new Reservations();
                    e.setUid(d.getUid());          // 主键必填
                    e.setClassroomId(d.getClassroomId());
                    e.setStartTime(d.getStartTime());
                    e.setEndTime(d.getEndTime());
                    e.setUid(d.getUid());
                    return e;
                })
                .collect(Collectors.toList());

        boolean success = updateBatchById(list);
        return success ? list.size() : 0;
    }

    /* ===================== 3. 批量选择性更新（只更新非 null 字段） ===================== */
    public int batchSelectiveUpdate(List<ClassroomReserveDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return 0;
        }
        List<Reservations> list = dtoList.stream()
                .map(d -> {
                    Reservations e = new Reservations();
                    e.setUid(d.getUid());          // 主键必填
                    if (d.getClassroomId() != null) {
                        e.setClassroomId(d.getClassroomId());
                    }
                    if (d.getStartTime() != null) {
                        e.setStartTime(d.getStartTime());
                    }
                    if (d.getEndTime() != null) {
                        e.setEndTime(d.getEndTime());
                    }
                    if (d.getUid() != null) {
                        e.setUid(d.getUid());
                    }
                    return e;
                })
                .collect(Collectors.toList());

        boolean success = updateBatchById(list);
        return success ? list.size() : 0;
    }

    /* ===================== 4. 批量删除（单条/多条） ===================== */
    public int batchDeleteReservations(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        boolean success = removeByIds(idList);
        return success ? idList.size() : 0;
    }
}