package org.example.classroom.controller.besides;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.transfer.dto.selectDto.AdviceReplySelectDto;
import org.example.classroom.model.besides.AdviceReply;
import org.example.classroom.service.besides.AdviceReplyServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advice-reply")
public class AdviceReplyController {

    @Resource
    private AdviceReplyServiceImpl adviceReplyService;

    /* --------------------- 新增 --------------------- */
    @PostMapping
    public ResponseDto<Boolean> insertOne(@RequestBody AdviceReply adviceReply) {
        return ResponseDto.success(adviceReplyService.insertOne(adviceReply));
    }

    /* --------------------- 更新 --------------------- */
    @PutMapping("/updateById")
    public ResponseDto<Boolean> updateOne(@RequestBody AdviceReply adviceReply) {
        return ResponseDto.success(adviceReplyService.updateOne(adviceReply));
    }

    /* --------------------- 单条删除 --------------------- */
    @DeleteMapping("/{adviceReplyId}")
    public ResponseDto<Boolean> deleteOne(@PathVariable String adviceReplyId) {
        return ResponseDto.success(adviceReplyService.deleteOne(adviceReplyId));
    }

    /* --------------------- 批量删除 --------------------- */
    @DeleteMapping("/deleteByIds")
    public ResponseDto<Boolean> deleteBatch(@RequestBody List<String> ids) {
        return ResponseDto.success(adviceReplyService.deleteBatch(ids));
    }

    /* --------------------- 分页查询 --------------------- */
    @PostMapping("/page")
    public ResponseDto<IPage<AdviceReply>> page(@RequestBody AdviceReplySelectDto dto) {
        return ResponseDto.success(adviceReplyService.page(dto));
    }
    /* --------------------- adviceId查询 --------------------- */
    @PostMapping("/{adviceId}")   // 完整的映射：/api/advice-reply/{adviceId}
    public ResponseDto<AdviceReply> selectByAdviceId(@PathVariable String adviceId){
        return ResponseDto.success(adviceReplyService.selectByAdviceId(adviceId));
    }
}