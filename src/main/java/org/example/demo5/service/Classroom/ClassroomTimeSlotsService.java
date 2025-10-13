package org.example.demo5.service.Classroom;

import org.example.demo5.dto.ClassroomReserveDto;
import org.example.demo5.util.TimeSlotUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomTimeSlotsService {
    private void checkTimeRange(List<ClassroomReserveDto> dtoList) {
        for (ClassroomReserveDto d : dtoList) {
            String msg = TimeSlotUtil.validateRange(d.getStartTime().toLocalDateTime(), d.getEndTime().toLocalDateTime());
            if (msg != null) {
                throw new IllegalArgumentException(msg); // 统一抛运行时异常
            }
        }
    }
}
