package org.example.demo5.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeSlotUtil {

    private static final LocalTime OPEN  = LocalTime.of(8, 0);
    private static final LocalTime CLOSE = LocalTime.of(22, 0);

    /**
     * 校验时间段是否完全落在 08:00-22:00 内
     * @param start 预约开始时间
     * @param end   预约结束时间
     * @return 非法返回具体错误文案；合法返回 null
     */
    public static String validateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "预约开始或结束时间不能为空";
        }
        if (!end.isAfter(start)) {
            return "结束时间必须晚于开始时间";
        }

        LocalTime s = start.toLocalTime();
        LocalTime e = end.toLocalTime();

        if (s.isBefore(OPEN) || e.isAfter(CLOSE)) {
            return "预约时间段仅限 08:00 - 22:00";
        }
        return null; // 通过
    }
}