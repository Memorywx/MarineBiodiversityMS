package org.gdou.marine.biodiversity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ObservationSpeciesDTO {

    @NotNull(message = "物种ID不能为空")
    private Long speciesId;
    private Integer estimatedQuantity;
    private String behavior;
    private String remarks;
}
