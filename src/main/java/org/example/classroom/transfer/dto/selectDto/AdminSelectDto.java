package org.example.classroom.transfer.dto.selectDto;

import lombok.Data;

@Data
public class AdminSelectDto extends BaseSelectDto {

    private String realName;
    private String email;
    private String createBy;
    private String besides;

}
