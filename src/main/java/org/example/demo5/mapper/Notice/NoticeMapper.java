package org.example.demo5.mapper.Notice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.*;
import org.example.demo5.dto.SelectNoticeDto;
import org.example.demo5.model.Notice.Notice;
import org.example.demo5.model.Notice.NoticeImage;
import org.springframework.util.StringUtils;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 根据公告ID一次性查出公告+全部图片
     */
    @Select("SELECT n.*, i.id AS img_id, i.url, i.sort " +
            "FROM notice n " +
            "LEFT JOIN notice_image i ON n.notice_id = i.notice_id " +
            "WHERE n.notice_id = #{noticeId} " +
            "ORDER BY i.sort")
    @Results(id = "noticeWithImages", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "notice_id", property = "noticeId"),
            @Result(column = "level", property = "level"),
            @Result(column = "title", property = "title"),
            @Result(column = "context", property = "context"),
            @Result(column = "created_time", property = "createdTime"),
            @Result(column = "updated_time", property = "updatedTime"),
            // 嵌套集合：把多行图片聚合成 List
            @Result(property = "images",               // 要封装的属性
                    column = "notice_id",              // 把哪一列的值传给子查询
                    javaType = List.class,
                    many = @Many(select = "mapImage"))
    })
    Notice selectNoticeWithImages(@Param("noticeId") String noticeId);

    /**
     * 仅供 @Many 调用的子查询，把同一行映射成 NoticeImage
     */
    @Select("SELECT id, url, sort FROM notice_image " +
            "WHERE notice_id = #{noticeId} ORDER BY sort")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "url", property = "url"),
            @Result(column = "sort", property = "sort")
    })
    List<NoticeImage> mapImage(@Param("noticeId") String noticeId);

    /**
     * 根据条件查公告列表，字段为空就忽略
     */

    default List<Notice> findList(SelectNoticeDto q) {
        return this.selectList(
                Wrappers.<Notice>lambdaQuery()
                        // notice_id = #{noticeId}
                        .eq(StringUtils.hasText(q.getNoticeId()), Notice::getNoticeId, q.getNoticeId())
                        // title like concat('%', #{title}, '%')
                        .like(StringUtils.hasText(q.getTitle()), Notice::getTitle, q.getTitle())
                        // level = #{level}
                        .eq(StringUtils.hasText(q.getLevel()), Notice::getLevel, q.getLevel())
                        // create_by = #{createBy}
                        .eq(StringUtils.hasText(q.getCreateBy()), Notice::getCreateBy, q.getCreateBy())
                        // 内容模糊查：context like concat('%', #{text}, '%')
                        .like(StringUtils.hasText(q.getText()), Notice::getContext, q.getText())
                        // 按创建时间倒序，最新的在前
                        .orderByDesc(Notice::getCreatedTime)
        );
    }
}