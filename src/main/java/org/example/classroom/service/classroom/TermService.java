package org.example.classroom.service.classroom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.TermMapper;

import org.example.classroom.transfer.dto.selectDto.TermSelectDto;
import org.example.classroom.model.classroom.Term;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class TermService {

    @Resource
    private TermMapper termMapper;

    public Boolean insertTerm(Term dto) {
        Term term = new Term();
        term.setTermName(dto.getTermName());
        term.setTermId(UUID.randomUUID().toString());
        term.setStartTime(dto.getStartTime());
        term.setEndTime(dto.getEndTime());
        term.setIsActive(dto.getIsActive());

        // ✅ 计算当前周数
        LocalDate start = dto.getStartTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        int week = (int) (ChronoUnit.DAYS.between(start, LocalDate.now()) / 7) + 1;
        long weekNow=Math.max(week, 1);
        term.setWeekNow(weekNow);

        return termMapper.insert(term) > 0;
    }

    public Boolean updateTerm(Term dto) {
        Term term = new Term();

        term.setTermName(dto.getTermName());
        term.setTermId(dto.getTermId());
        term.setStartTime(dto.getStartTime());
        term.setEndTime(dto.getEndTime());

        term.setIsActive(dto.getIsActive());
        return termMapper.updateByTermId(term);
    }

    public Boolean deleteByTermId(String termId) {
        return termMapper.deleteByTermId(termId);
    }

    public IPage<Term> termPage(TermSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();

        Page<Term> pg = new Page<>(page, size);
        LambdaQueryWrapper<Term> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(dto.getTermId()), Term::getTermId, dto.getTermId())
                .eq(dto.getIsActive() != null, Term::getIsActive, dto.getIsActive())

                .orderByDesc(Term::getCreatedTime);

        return termMapper.selectPage(pg, wrapper);


    }

    private void refreshActiveTermWeekNow() {
        Term term = termMapper.getActiveTerm();
        if (term == null) return;

        LocalDate start = term.getStartTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        int week = (int) (ChronoUnit.DAYS.between(start, LocalDate.now()) / 7) + 1;
        int weekNow = Math.max(week, 1);

        // 只更新 week_now 字段
        termMapper.updateActiveTermWeekNow(weekNow);


    }
    @Scheduled(cron = "0 0 0 * * ?")   // 每天 00:00:00
    public void autoRefreshWeekNow() {
        refreshActiveTermWeekNow();
    }
    public void manualRefreshWeekNow() {
        refreshActiveTermWeekNow();
    }
}