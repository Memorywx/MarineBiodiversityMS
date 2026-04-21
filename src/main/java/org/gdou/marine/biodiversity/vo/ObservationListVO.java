package org.gdou.marine.biodiversity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ObservationListVO {

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationTime;
    private String locationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String ecosystemName;
    private String observer;
    private Integer speciesCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
