package org.gdou.marine.biodiversity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EcosystemVO {

    private Long id;
    private String name;
    private String type;
    private String description;
    private String geoRange;
    private String environmentFeatures;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
