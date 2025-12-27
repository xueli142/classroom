package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.classroom.model.classroom.Thing;
import org.example.classroom.service.classroom.serviceImpl.ThingServiceImpl;
import org.example.classroom.transfer.dto.insertDto.ThingDto;
import org.example.classroom.transfer.dto.selectDto.ThingSelectDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thing")
@RequiredArgsConstructor
public class ThingController {

    private final ThingServiceImpl thingService;

    /* ------------- 查询 ------------- */
    @PostMapping("/page")                      // 分页+条件
    public IPage<Thing> page(@RequestBody ThingSelectDto dto) {
        return thingService.pageByDto(dto);
    }

    @GetMapping("/{id}")                       // 单条详情
    public Thing one(@PathVariable String id) {
        return thingService.getById(id);
    }

    /* ------------- 新增 ------------- */
    @PostMapping                       // 单条新增
    public Boolean save(@RequestBody ThingDto dto) {
        return thingService.insertOne(dto);
    }

    @PostMapping("/batch")                     // 批量新增
    public Boolean batch(@RequestBody List<ThingDto> dtoList) {
        return thingService.insertBatch(dtoList);
    }

    /* ------------- 修改 ------------- */
    @PutMapping                       // 全量更新（主键在 Thing 实体里）
    public Boolean update(@RequestBody Thing thing) {
        return thingService.updateById(thing);
    }

    /* ------------- 删除 ------------- */
    @DeleteMapping("/{id}")                    // 单删
    public Boolean del(@PathVariable String id) {
        return thingService.deleteByThingId(id);
    }

    @DeleteMapping                       // 批量删除
    public Boolean delBatch(@RequestBody List<String> ids) {
        return thingService.deleteByThingIds(ids);
    }
}