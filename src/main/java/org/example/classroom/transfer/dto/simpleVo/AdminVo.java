package org.example.classroom.transfer.dto.simpleVo;

import lombok.Data;

@Data
public class AdminVo extends BaseUserVo {

    private String realName;
    private String email;
    private String createBy;
    private String besides;
}
