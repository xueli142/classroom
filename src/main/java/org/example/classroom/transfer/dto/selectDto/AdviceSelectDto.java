package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;


@Data
public class AdviceSelectDto {
    private Long size;
    private Long page;
    private String adviceId;
    private String name;
    private String userId;
    private String title;
    private String type;
    private String advice;
    private Boolean status;


}
