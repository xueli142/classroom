package org.example.demo5.mapper.Notice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.demo5.model.Notice.NoticeImage;

@Mapper
public interface NoticeImageMapper extends BaseMapper<NoticeImage> {
    default int insertNoticeImage(NoticeImage noticeImage) {
        return this.insert(noticeImage);
    }




















}
