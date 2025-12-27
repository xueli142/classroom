package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

@Data
public class AdviceReplySelectDto {

    private Long size;
    private Long page;
    private String adviceReplyId;
    private String adviceId;
    private String title;
    private String reply;

}
