package org.gdou.marine.biodiversity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String realName;
    private String email;
    private String avatar;
    private Integer role;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
