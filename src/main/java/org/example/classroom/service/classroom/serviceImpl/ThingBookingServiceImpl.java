package org.example.classroom.service.classroom.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.ThingBookingMapper;
import org.example.classroom.dao.classroom.ThingMapper;
import org.example.classroom.model.classroom.Thing;
import org.example.classroom.model.classroom.ThingBooking;
import org.example.classroom.service.classroom.service.ThingBookingService;
import org.example.classroom.transfer.dto.insertDto.ThingBookingDto;
import org.example.classroom.transfer.dto.selectDto.ThingBookingSelectDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ThingBookingServiceImpl
        extends ServiceImpl<ThingBookingMapper, ThingBooking>
        implements ThingBookingService {

    @Resource
    private ThingBookingMapper thingBookingMapper;
    @Resource
    private ThingMapper thingMapper;

    /* ======================== 构造器 ======================== */
    public ThingBooking build(ThingBookingDto dto) {
        Thing thing = new Thing();
        thing.setThingId(dto.getThingId());
        thing.setNeedBooking(false);
        thingMapper.updateByThingId(thing);



        ThingBooking entity = new ThingBooking();
        entity.setThingBookingId(UUID.randomUUID().toString());
        entity.setUserId(dto.getUserId());

        /* 计算 endTime = startTime + time 天 */
        java.sql.Date startSqlDate = dto.getStartTime();
        LocalDate startLocal = startSqlDate.toLocalDate();
        LocalDate endLocal   = startLocal.plusDays(dto.getTime());   // time 单位：天
        java.sql.Date endSqlDate = java.sql.Date.valueOf(endLocal);

        entity.setStartTime(startSqlDate);
        entity.setEndTime(endSqlDate);
        entity.setThingId(dto.getThingId());
        entity.setActive(true);
        entity.setTime(dto.getTime());
        entity.setBesides(dto.getBesides());
        return entity;
    }

    /* ======================== 单条插入 ======================== */
    public Boolean insertOne(ThingBookingDto dto) {
        return thingBookingMapper.insert(build(dto)) > 0;
    }

    /* ======================== 批量插入 ======================== */
    @Transactional
    public Boolean insertBatch(List<ThingBookingDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return true;
        }
        List<ThingBooking> list = dtoList.stream()
                .map(this::build)
                .toList();
        return saveBatch(list);
    }

    /* ======================== 分页查询 ======================== */
    public IPage<ThingBooking> pageByDto(ThingBookingSelectDto dto) {
        long current = dto.getPage() == null ? 1L : dto.getPage();
        long size    = dto.getSize() == null ? 10L : dto.getSize();
        Page<ThingBooking> page = new Page<>(current, size);

        return new LambdaQueryChainWrapper<>(thingBookingMapper)
                .eq(dto.getThingBookingId() != null, ThingBooking::getThingBookingId, dto.getThingBookingId())
                .eq(dto.getUserId() != null, ThingBooking::getUserId, dto.getUserId())
                .eq(dto.getStartTime() != null, ThingBooking::getStartTime, dto.getStartTime())
                .eq(dto.getEndTime() != null, ThingBooking::getEndTime, dto.getEndTime())
                .eq(dto.getActive() != null, ThingBooking::getActive, dto.getActive())
                .eq(dto.getTime() != null, ThingBooking::getTime, dto.getTime())
                .like(dto.getBesides() != null, ThingBooking::getBesides, dto.getBesides())
                .orderByDesc(ThingBooking::getUpdatedTime)
                .page(page);
    }
}