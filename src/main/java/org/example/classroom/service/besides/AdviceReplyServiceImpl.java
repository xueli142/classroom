package org.example.classroom.service.besides;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.classroom.dao.besides.AdviceMapper;
import org.example.classroom.dao.besides.AdviceReplyMapper;

import org.example.classroom.model.besides.Advice;
import org.example.classroom.model.besides.AdviceReply;
import org.example.classroom.transfer.dto.selectDto.AdviceReplySelectDto;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class AdviceReplyServiceImpl
        extends ServiceImpl<AdviceReplyMapper, AdviceReply>
        implements AdviceReplyService {

    @Resource
    private AdviceReplyMapper adviceReplyMapper;
    @Resource
    private AdviceMapper adviceMapper;

    /* -------------------------------------------------
       新增：单条插入
       ------------------------------------------------- */
    public Boolean insertOne(AdviceReply adviceReply) {

        AdviceReply data = new AdviceReply();
        // 主键
        data.setAdviceReplyId(UUID.randomUUID().toString());
        // 业务字段
        data.setAdviceId(adviceReply.getAdviceId());
        data.setTitle(adviceReply.getTitle());
        data.setReply(adviceReply.getReply());
        // createdTime / updatedTime 由 MP 自动填充
        Boolean  result = adviceMapper.updateStatusByAdviceId(adviceReply.getAdviceId());
        return (adviceReplyMapper.insert(data) > 0)&&result;
    }

    /* -------------------------------------------------
       删除：单条 / 批量
       ------------------------------------------------- */
    public Boolean deleteOne(String adviceReplyId) {
        return adviceReplyMapper.deleteByAdviceReplyId(adviceReplyId);
    }

    public Boolean deleteBatch(List<String> adviceReplyIds) {
        return adviceReplyMapper.deleteByReplyIds(adviceReplyIds);
    }

    /* -------------------------------------------------
       更新：整单更新（按 adviceReplyId）
       ------------------------------------------------- */
    public Boolean updateOne(AdviceReply adviceReply) {
        return adviceReplyMapper.updateByAdviceReplyId(adviceReply);
    }

    /* -------------------------------------------------
       分页查询（对齐 AdviceReplyDto 字段）
       ------------------------------------------------- */
    public IPage<AdviceReply> page(AdviceReplySelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();
        Page<AdviceReply> pg = new Page<>(page, size);

        LambdaQueryWrapper<AdviceReply> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(dto.getAdviceReplyId()),
                AdviceReply::getAdviceReplyId, dto.getAdviceReplyId());
        wrapper.eq(StringUtils.hasText(dto.getAdviceId()),
                AdviceReply::getAdviceId, dto.getAdviceId());
        wrapper.eq(StringUtils.hasText(dto.getTitle()),
                AdviceReply::getTitle, dto.getTitle());
        wrapper.eq(StringUtils.hasText(dto.getReply()),
                AdviceReply::getReply, dto.getReply());

        return adviceReplyMapper.selectPage(pg, wrapper);
    }

    public AdviceReply selectByAdviceId(String adviceId){
        return adviceReplyMapper.selectByAdviceId(adviceId);
    }
}