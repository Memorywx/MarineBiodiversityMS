package org.gdou.marine.biodiversity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("observation")
public class Observation extends BaseEntity {

    private LocalDateTime observationTime;
    private String locationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long ecosystemId;
    private String observer;
    private Long observerUserId;
    private BigDecimal waterTemperature;
    private BigDecimal salinity;
    private BigDecimal phValue;
    private BigDecimal depth;
    private String remarks;
    private Long createBy;
}
