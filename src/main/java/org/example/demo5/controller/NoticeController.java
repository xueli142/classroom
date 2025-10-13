package org.example.demo5.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.demo5.dto.SelectNoticeDto;
import org.example.demo5.model.Notice.Notice;
import org.example.demo5.model.Notice.NoticeImage;
import org.example.demo5.mapper.Notice.NoticeMapper;
import org.example.demo5.mapper.Notice.NoticeImageMapper;
import org.example.demo5.dto.ResponseDto;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private NoticeImageMapper noticeImageMapper;

    /* ========== 查 ========== */

    /**
     * 分页+条件查询
     */
    @GetMapping("/page")
    public ResponseDto<IPage<Notice>> page(@RequestParam(defaultValue = "1") long current,
                                 @RequestParam(defaultValue = "10") long size,
                                 SelectNoticeDto q) {
        IPage<Notice> page = noticeMapper.selectPage(
                new Page<>(current, size),
                Wrappers.<Notice>lambdaQuery()
                        .eq(StringUtils.hasText(q.getNoticeId()), Notice::getNoticeId, q.getNoticeId())
                        .like(StringUtils.hasText(q.getTitle()), Notice::getTitle, q.getTitle())
                        .eq(StringUtils.hasText(q.getLevel()), Notice::getLevel, q.getLevel())
                        .eq(StringUtils.hasText(q.getCreateBy()), Notice::getCreateBy, q.getCreateBy())
                        .like(StringUtils.hasText(q.getText()), Notice::getContext, q.getText())
                        .orderByDesc(Notice::getCreatedTime)
        );
        return ResponseDto.success(page);
    }

    /**
     * 列表查询（不分页）
     */
    @GetMapping("/list")
    public ResponseDto<List<Notice>> list(SelectNoticeDto q) {
        List<Notice> list = noticeMapper.findList(q);   // 就是上一段给的 default 方法
        return ResponseDto.success(list);
    }

    /**
     * 单条详情（含图片）
     */
    @GetMapping("/{noticeId}")
    public ResponseDto<Notice> detail(@PathVariable String noticeId) {
        Notice n = noticeMapper.selectOne(
                Wrappers.<Notice>lambdaQuery().eq(Notice::getNoticeId, noticeId));
        if (n == null) {
            return ResponseDto.error("公告不存在");
        }
        List<NoticeImage> images = noticeImageMapper.selectList(
                Wrappers.<NoticeImage>lambdaQuery()
                        .eq(NoticeImage::getNoticeId, noticeId)
                        .orderByAsc(NoticeImage::getSort));
        n.setImages(images);   // 实体里 exist = false 的字段
        return ResponseDto.success(n);
    }

    /* ========== 增 ========== */

    /**
     * 新增公告
     */
    @PostMapping
    public ResponseDto<String> create(@RequestBody Notice notice) {
        notice.setId(null);   // 自增主键
        noticeMapper.insert(notice);
        return ResponseDto.success(notice.getNoticeId()); // 返回业务主键
    }

    /* ========== 改 ========== */

    /**
     * 修改公告
     */
    @PutMapping("/{noticeId}")
    public ResponseDto<Void> update(@PathVariable String noticeId,
                          @RequestBody Notice notice) {
        Notice exist = noticeMapper.selectOne(
                Wrappers.<Notice>lambdaQuery().eq(Notice::getNoticeId, noticeId));
        if (exist == null) {
            return ResponseDto.error("公告不存在");
        }
        notice.setId(exist.getId());   // MP 根据主键更新
        notice.setNoticeId(noticeId);  // 防止被篡改
        noticeMapper.updateById(notice);
        return ResponseDto.success(null);
    }

    /* ========== 删 ========== */

    /**
     * 删除公告（级联删除图片需配置外键或手动清）
     */
    @DeleteMapping("/{noticeId}")
    public ResponseDto<Void> delete(@PathVariable String noticeId) {
        Notice exist = noticeMapper.selectOne(
                Wrappers.<Notice>lambdaQuery().eq(Notice::getNoticeId, noticeId));
        if (exist == null) {
            return ResponseDto.error("公告不存在");
        }
        // 先删图片
        noticeImageMapper.delete(
                Wrappers.<NoticeImage>lambdaQuery().eq(NoticeImage::getNoticeId, noticeId));
        // 再删公告
        noticeMapper.deleteById(exist.getId());
        return ResponseDto.success(null);
    }
}