package org.example.demo5.service.Notice;

import jakarta.annotation.Resource;
import org.example.demo5.mapper.Notice.NoticeMapper;
import org.example.demo5.model.Notice.Notice;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

    public Notice detail(String noticeId) {
        return noticeMapper.selectNoticeWithImages(noticeId);
    }
}