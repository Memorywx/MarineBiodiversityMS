package org.gdou.marine.biodiversity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ObservationDetailVO {

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationTime;
    private String locationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long ecosystemId;
    private String ecosystemName;
    private String observer;
    private BigDecimal waterTemperature;
    private BigDecimal salinity;
    private BigDecimal phValue;
    private BigDecimal depth;
    private String remarks;
    private List<ObservationSpeciesVO> speciesList;
    private Long createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
