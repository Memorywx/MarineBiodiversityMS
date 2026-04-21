package org.gdou.marine.biodiversity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ObservationCreateDTO {

    @NotNull(message = "观测时间不能为空")
    private LocalDateTime observationTime;

    private String locationName;

    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    private Long ecosystemId;
    private String observer;
    private BigDecimal waterTemperature;
    private BigDecimal salinity;
    private BigDecimal phValue;
    private BigDecimal depth;
    private String remarks;
    private List<ObservationSpeciesDTO> speciesList;
}
