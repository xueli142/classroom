package org.example.classroom.dao.besides;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.classroom.model.besides.Event;

import java.util.List;

public interface EventMapper extends BaseMapper<Event> {

    default Boolean deleteByEventId(String eventId) {
        return delete(Wrappers.<Event>lambdaQuery()
                .eq(Event::getEventId, eventId)) > 0;
    }

    default Boolean deleteByEventIds(List<String> eventIds) {
        return delete(Wrappers.<Event>lambdaQuery()
                .in(Event::getEventId, eventIds)) > 0;
    }

    default Boolean updateByEventId(Event event) {
        return update(event, Wrappers.<Event>lambdaQuery()
                .eq(Event::getEventId, event.getEventId())) > 0;
    }

    @Update("UPDATE event " +
            "SET number = (SELECT COUNT(*) " +
            "              FROM student_event s " +
            "              WHERE s.event_id = event.event_id) " +
            "WHERE event_id = #{eventId}")
    int refreshCountByEventId(@Param("eventId") String eventId);
}