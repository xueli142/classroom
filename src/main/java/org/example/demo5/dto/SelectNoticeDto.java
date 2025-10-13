package org.example.demo5.dto;

import lombok.Data;

@Data
public class SelectNoticeDto {
    private String title;
    private String level;
    private String createBy;
    private String noticeId;
    private String text;
}
