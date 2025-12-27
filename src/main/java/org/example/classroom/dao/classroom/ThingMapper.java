package org.example.classroom.dao.classroom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.classroom.model.classroom.Thing;
import java.util.List;


public interface ThingMapper extends BaseMapper<Thing> {

    /** 根据 thingId 删除单条记录 */
    default Boolean deleteByThingId(String thingId) {
        return delete(Wrappers.<Thing>lambdaQuery()
                .eq(Thing::getThingId, thingId)) > 0;
    }

    /** 根据 thingId 列表批量删除 */
    default Boolean deleteByThingIds(List<String> thingIds) {
        return delete(Wrappers.<Thing>lambdaQuery()
                .in(Thing::getThingId, thingIds)) > 0;
    }

    /** 根据 thingId 更新整条记录 */
    default Boolean updateByThingId(Thing thing) {
        return update(thing, Wrappers.<Thing>lambdaUpdate()
                .eq(Thing::getThingId, thing.getThingId())) > 0;
    }
}