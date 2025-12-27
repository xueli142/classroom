package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

import java.util.Date;

@Data
public class TermSelectDto {
    private String TermName;
    private Long page;
    private Long size;
    private String termId;
    private Date startTime;
    private Date endTime;
    private Long weekNow;
    private Boolean IsActive;

}
