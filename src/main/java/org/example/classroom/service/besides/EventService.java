package org.example.classroom.service.besides;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.besides.EventMapper;
import org.example.classroom.transfer.dto.insertDto.EventDto;
import org.example.classroom.transfer.dto.selectDto.EventSelectDto;
import org.example.classroom.model.besides.Event;
import org.example.classroom.util.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Resource
    EventMapper eventMapper;
    @Resource
    ImageUtil imageUtil;

    /* ============================= 新增 ============================= */
    public Boolean insertEvent(EventDto dto, MultipartFile file) {
        String url = imageUtil.saveImage(file);
        String eventId = UUID.randomUUID().toString();

        Event event = new Event();
        event.setEventId(eventId);
        event.setUserId(dto.getUserId());
        event.setEventName(dto.getEventName());
        event.setUserName(dto.getUserName());
        event.setDescription(dto.getDescription());
        event.setNeedAudit(dto.getNeedAudit());
        event.setBesides(dto.getBesides());
        event.setImageUrl(url);
        event.setNumber(0L);
        return eventMapper.insert(event) > 0;
    }

    /* ============================= 更新 ============================= */
    public Boolean updateEvent(EventDto dto, MultipartFile file, String oldName) {
        Event event = new Event();
        event.setEventId(dto.getEventId());
        event.setUserId(dto.getUserId());
        event.setEventName(dto.getEventName());
        event.setUserName(dto.getUserName());
        event.setDescription(dto.getDescription());
        event.setNeedAudit(dto.getNeedAudit());
        event.setBesides(dto.getBesides());
        event.setNumber(dto.getNumber());

        if (file != null) {
            String url = imageUtil.changeImage(file, oldName);
            event.setImageUrl(url);
        }
        return eventMapper.updateByEventId(event);
    }

    public Boolean deleteByEventId(String eventId) {
        return eventMapper.deleteByEventId(eventId);
    }

    public Boolean deleteByEventIds(List<String> eventIds) {
        return eventMapper.deleteByEventIds(eventIds);
    }

    /* ============================= 分页 ============================= */
    public IPage<Event> eventIPage(EventSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();

        Page<Event> pg = new Page<>(page, size);
        LambdaQueryWrapper<Event> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(dto.getEventName()), Event::getEventName, dto.getEventName())
                .like(StringUtils.hasText(dto.getUserName()), Event::getUserName, dto.getUserName())
                .eq(StringUtils.hasText(dto.getEventId()), Event::getEventId, dto.getEventId())
                .like(StringUtils.hasText(dto.getDescription()), Event::getDescription, dto.getDescription())
                .eq(dto.getNeedAudit() != null, Event::getNeedAudit, dto.getNeedAudit())
                .like(StringUtils.hasText(dto.getBesides()), Event::getBesides, dto.getBesides())
                .eq(dto.getNumber() != null, Event::getNumber, dto.getNumber())
                .orderByDesc(Event::getCreatedTime);

        return eventMapper.selectPage(pg, wrapper);
    }
}