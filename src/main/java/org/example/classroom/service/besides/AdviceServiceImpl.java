package org.example.classroom.service.besides;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.classroom.dao.besides.AdviceMapper;
import org.example.classroom.transfer.dto.selectDto.AdviceSelectDto;
import org.example.classroom.model.besides.Advice;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class AdviceServiceImpl extends ServiceImpl<AdviceMapper, Advice> implements AdviceService {
    @Resource
    private AdviceMapper adviceMapper;

    public Boolean  insertOne(Advice advice){
        Advice data = new Advice();
        data.setAdviceId(UUID.randomUUID().toString());
        data.setStatus(false);
        data.setName(advice.getName());
        data.setTitle(advice.getTitle());
        data.setType(advice.getType());
        data.setUserId(advice.getUserId());
        data.setAdvice(advice.getAdvice());

        return adviceMapper.insert(data)>0;

    }
public Boolean deleteOne(String id){
        return adviceMapper.deleteByAdviceId(id);
}

public Boolean deleteBatch(List<String> ids){
        return adviceMapper.deleteByIds(ids);

}

public Boolean updateOne(Advice advice){
        return adviceMapper.updateByAdviceId(advice);
}

    public IPage<Advice> page(AdviceSelectDto dto) {
        // 1. 分页参数
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();
        Page<Advice> pg = new Page<>(page, size);

        // 2. 查询条件
        LambdaQueryWrapper<Advice> wrapper = new LambdaQueryWrapper<>();

        // String 类型：非空才 eq
        wrapper.eq(StringUtils.hasText(dto.getAdviceId()), Advice::getAdviceId, dto.getAdviceId());
        wrapper.eq(StringUtils.hasText(dto.getUserId()),   Advice::getUserId,   dto.getUserId());
        wrapper.eq(StringUtils.hasText(dto.getName()),     Advice::getName,     dto.getName());
        wrapper.eq(StringUtils.hasText(dto.getTitle()),    Advice::getTitle,    dto.getTitle());
        wrapper.eq(StringUtils.hasText(dto.getType()),     Advice::getType,     dto.getType());
        wrapper.eq(StringUtils.hasText(dto.getAdvice()),   Advice::getAdvice,   dto.getAdvice());

        // Boolean 类型：不为 null 才 eq
        wrapper.eq(dto.getStatus() != null, Advice::getStatus, dto.getStatus());

        // 3. 执行分页查询
        return adviceMapper.selectPage(pg, wrapper);
    }

}
