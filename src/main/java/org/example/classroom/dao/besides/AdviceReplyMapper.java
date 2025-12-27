package org.example.classroom.dao.besides;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.classroom.model.besides.AdviceReply;
import org.example.classroom.model.user.User;

import java.util.List;

public interface AdviceReplyMapper extends BaseMapper<AdviceReply> {

    /**
     * 根据主键 adviceReplyId 单条删除
     */
    default Boolean deleteByAdviceReplyId(String adviceReplyId) {
        return delete(Wrappers.<AdviceReply>lambdaQuery()
                .eq(AdviceReply::getAdviceReplyId, adviceReplyId)) > 0;
    }

    /**
     * 根据 adviceReplyId 批量删除
     */
    default Boolean deleteByReplyIds(List<String> adviceReplyIds) {
        return delete(Wrappers.<AdviceReply>lambdaQuery()
                .in(AdviceReply::getAdviceReplyId, adviceReplyIds)) > 0;
    }

    /**
     * 根据 adviceReplyId 更新整条记录
     */
    default Boolean updateByAdviceReplyId(AdviceReply adviceReply) {
        return update(adviceReply,
                Wrappers.<AdviceReply>lambdaUpdate()
                        .eq(AdviceReply::getAdviceReplyId, adviceReply.getAdviceReplyId())) > 0;
    }

    // Mapper 接口里直接加 default 方法

    default AdviceReply selectByAdviceId(String adviceId){
        return this.selectOne(Wrappers.<AdviceReply>lambdaQuery()
                .eq(AdviceReply::getAdviceId,adviceId));
    }
}