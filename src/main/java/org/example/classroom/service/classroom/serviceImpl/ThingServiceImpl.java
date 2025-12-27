package org.example.classroom.service.classroom.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.ThingMapper;
import org.example.classroom.model.classroom.Thing;
import org.example.classroom.service.classroom.service.ThingService;
import org.example.classroom.transfer.dto.insertDto.ThingDto;
import org.example.classroom.transfer.dto.selectDto.ThingSelectDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ThingServiceImpl
extends ServiceImpl<ThingMapper, Thing> implements ThingService {
    @Resource
    ThingMapper thingMapper;

    public Boolean  deleteByThingId(String id){
        return thingMapper.deleteByThingId(id);
    }

    public Boolean deleteByThingIds(List<String> ids){

        return thingMapper.deleteByThingIds(ids);
    }

    public Boolean updateByThingId(Thing thing ){
        return thingMapper.updateByThingId(thing);
    }

    public Thing build(ThingDto dto) {
        Thing thing = new Thing();
        thing.setThingId(UUID.randomUUID().toString());   // 主键本地生成
        thing.setClassroomId(dto.getClassroomId());
        thing.setType(dto.getType());
        thing.setThingName(dto.getThingName());
        thing.setNeedBooking(dto.getNeedBooking());
        thing.setNeedUse(dto.getNeedUse());
        thing.setBesides(dto.getBesides());
        thing.setDescription(dto.getDescription());

        return thing;
    }

    public Boolean insertOne(ThingDto thingDto) {
        Thing thing = build(thingDto);
        return thingMapper.insert(thing) > 0;
    }

    @Transactional
    public Boolean insertBatch(List<ThingDto> dtoList){
        if(CollectionUtils.isEmpty(dtoList)){
        return true;

    }

        List<Thing> things = dtoList.stream()
                .map(this::build)
                .toList();

        return saveBatch(things);
    }
    public IPage<Thing> pageByDto(ThingSelectDto dto) {
        // 如果前端没传分页参数，默认第 1 页、10 条
        long current = dto.getPage() == null ? 1L : dto.getPage();
        long size    = dto.getSize() == null ? 10L : dto.getSize();
        Page<Thing> page = new Page<>(current, size);

        return new LambdaQueryChainWrapper<>(thingMapper)
                .eq(dto.getThingId() != null, Thing::getThingId, dto.getThingId())
                .eq(dto.getClassroomId() != null, Thing::getClassroomId, dto.getClassroomId())
                .eq(dto.getType() != null, Thing::getType, dto.getType())
                .like(dto.getThingName() != null, Thing::getThingName, dto.getThingName())
                .eq(dto.getNeedBooking() != null, Thing::getNeedBooking, dto.getNeedBooking())
                .eq(dto.getNeedUse() != null, Thing::getNeedUse, dto.getNeedUse())
                .like(dto.getBesides() != null, Thing::getBesides, dto.getBesides())
                .like(dto.getDescription() != null, Thing::getDescription, dto.getDescription())

                .orderByDesc(Thing::getUpdatedTime)
                .page(page);          // 关键：链式调 page 返回 IPage
    }
}
