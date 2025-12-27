package org.example.classroom.dao.besides;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.classroom.model.besides.Advice;

import java.util.List;

public interface AdviceMapper extends BaseMapper<Advice> {


    default Boolean deleteByAdviceId(String id){
        return delete(Wrappers.<Advice>lambdaQuery()
                .eq(Advice::getAdviceId,id))>0;
    }

    default Boolean deleteByIds(List<String> ids){
        return delete(Wrappers.<Advice>lambdaQuery()
                .in(Advice::getAdviceId,ids))>0;
    }

    default Boolean updateByAdviceId(Advice advice){
        return update(advice,
                Wrappers.<Advice>lambdaUpdate()
                .eq(Advice::getAdviceId,advice.getAdviceId()))>0;
    }

    default Boolean updateStatusByAdviceId(String adviceId) {
        return update(
                null,                                      // 不传递实体，纯 set 指定
                Wrappers.<Advice>lambdaUpdate()
                        .set(Advice::getStatus, true) // 要更新的字段
                        .eq(Advice::getAdviceId, adviceId) // 条件字段
        ) > 0;
    }

}
