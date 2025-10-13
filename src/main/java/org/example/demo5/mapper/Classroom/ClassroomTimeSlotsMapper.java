package org.example.demo5.mapper.Classroom;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.demo5.dto.TimeDto;
import org.example.demo5.model.Classroom.ClassroomTimeSlots;

import java.util.List;
import java.util.stream.Collectors;

public interface ClassroomTimeSlotsMapper extends BaseMapper<ClassroomTimeSlots> {


    /* ========================== 删 ========================== */
    /**
     * 按 classroomId 物理删除
     */
    default int deleteByClassroomId(String classroomId) {
        return this.delete(new QueryWrapper<ClassroomTimeSlots>()
                .eq("classroom_id", classroomId));
    }

    /**
     * 按 classroomId 逻辑删除（deleted=1）
     */
    default int logicDeleteByClassroomId(String classroomId) {
        return this.update(null, new UpdateWrapper<ClassroomTimeSlots>()
                .eq("classroom_id", classroomId)
                .set("deleted", 1));
    }


    /* ========================== 查 ========================== */
    /**
     * 按 classroomId 查列表
     */
    default List<ClassroomTimeSlots> listByClassroomId(String classroomId) {
        return this.selectList(new QueryWrapper<ClassroomTimeSlots>()
                .eq("classroom_id", classroomId)
                .orderByAsc("weekday", "start_time"));
    }

    /**
     * 按 classroomId 查一条（业务上唯一时可用）
     */
    default ClassroomTimeSlots getOneByClassroomId(String classroomId) {
        return this.selectOne(new QueryWrapper<ClassroomTimeSlots>()
                .eq("classroom_id", classroomId)
                .last("limit 1"));
    }

    /**
     * 按 classroomId 计数
     */
    default long countByClassroomId(String classroomId) {
        return this.selectCount(new QueryWrapper<ClassroomTimeSlots>()
                .eq("classroom_id", classroomId));
    }
    default int batchInsertByClassroomId(List<TimeDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return 0;
        }
        List<ClassroomTimeSlots> list = dtoList.stream()
                .map(d -> {
                    ClassroomTimeSlots e = new ClassroomTimeSlots();
                    e.setClassroomId(d.getClassroomId());
                    e.setStartTime(d.getStartTime());
                    e.setEndTime(d.getEndTime());
                    e.setDayOfWeek(d.getDayOfWeek());
                    return e;
                })
                .collect(Collectors.toList());
        // 如果没有引入 MP 批量插插件，就用循环 insert
        int rows = 0;
        for (ClassroomTimeSlots e : list) {
            rows += this.insert(e);
        }
        return rows;
    }
}


