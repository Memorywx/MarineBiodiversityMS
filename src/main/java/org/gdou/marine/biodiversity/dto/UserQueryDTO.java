package org.gdou.marine.biodiversity.dto;

import lombok.Data;

@Data
public class UserQueryDTO {

    private String keyword;
    private Integer role;
    private Integer status;
    private long current = 1;
    private long size = 10;
}
