package org.example.classroom.controller.besides;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.selectDto.AdviceSelectDto;
import org.example.classroom.model.besides.Advice;
import org.example.classroom.service.besides.AdviceServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 意见反馈管理控制器
 * 严格按 AdviceServiceImpl 已有方法暴露
 */
@RestController
@RequestMapping("/api/advice")
public class AdviceController {

    @Resource
    private AdviceServiceImpl adviceService;

    /* -----------------------------------------------------------
       新增单条
       ----------------------------------------------------------- */
    @PostMapping("/insert")
    public ResponseDto<Boolean> insertOne(@RequestBody Advice advice) {
        return ResponseDto.success(adviceService.insertOne(advice));
    }

    /* -----------------------------------------------------------
       单条更新（by adviceId）
       ----------------------------------------------------------- */
    @PutMapping("/updateById")
    public ResponseDto<Boolean> updateOne(@RequestBody Advice advice) {
        return ResponseDto.success(adviceService.updateOne(advice));
    }

    /* -----------------------------------------------------------
       单条删除（by adviceId）
       ----------------------------------------------------------- */
    @DeleteMapping("/{adviceId}")
    public ResponseDto<Boolean> deleteOne(@PathVariable String adviceId) {
        return ResponseDto.success(adviceService.deleteOne(adviceId));
    }

    /* -----------------------------------------------------------
       批量删除（by adviceIds）
       ----------------------------------------------------------- */
    @DeleteMapping("/deleteByIds")
    public ResponseDto<Boolean> deleteBatch(@RequestBody List<String> ids) {
        return ResponseDto.success(adviceService.deleteBatch(ids));
    }

    /* -----------------------------------------------------------
       通用分页查询
       ----------------------------------------------------------- */
    @PostMapping("/page")
    public ResponseDto<IPage<Advice>> page(@RequestBody AdviceSelectDto dto) {
        return ResponseDto.success(adviceService.page(dto));
    }
}