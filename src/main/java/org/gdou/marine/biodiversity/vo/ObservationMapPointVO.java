package org.gdou.marine.biodiversity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ObservationMapPointVO {

    private Long observationId;
    private BigDecimal lat;
    private BigDecimal lng;
    private String locationName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationTime;
    private String ecosystemName;
    private Integer speciesCount;
}
