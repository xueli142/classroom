package org.example.classroom.controller.classroom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.selectDto.TermSelectDto;
import org.example.classroom.model.classroom.Term;
import org.example.classroom.service.classroom.TermService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/term")
public class TermController {

    @Resource
    private TermService termService;

    /* ===========================================================
       新增
       =========================================================== */
    @PostMapping
    public ResponseDto<Boolean> insert(@RequestBody Term dto) {
        return ResponseDto.success(termService.insertTerm(dto));
    }

    /* ===========================================================
       更新
       =========================================================== */
    @PutMapping
    public ResponseDto<Boolean> update(@RequestBody Term dto) {
        return ResponseDto.success(termService.updateTerm(dto));
    }

    /* ===========================================================
       删除
       =========================================================== */
    @DeleteMapping("/{termId}")
    public ResponseDto<Boolean> deleteOne(@PathVariable String termId) {
        return ResponseDto.success(termService.deleteByTermId(termId));
    }

    /* ===========================================================
       分页
       =========================================================== */
    @PostMapping("/page")
    public ResponseDto<IPage<Term>> page(@RequestBody TermSelectDto dto) {
        return ResponseDto.success(termService.termPage(dto));
    }
    @PostMapping("/term/refresh-week")
    public ResponseDto<String> manualRefreshWeekNow() {
        termService.manualRefreshWeekNow();
        return ResponseDto.success("周数已手动刷新");
    }
}