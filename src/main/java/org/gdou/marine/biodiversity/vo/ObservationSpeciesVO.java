package org.gdou.marine.biodiversity.vo;

import lombok.Data;

@Data
public class ObservationSpeciesVO {

    private Long speciesId;
    private String chineseName;
    private String scientificName;
    private Integer estimatedQuantity;
    private String behavior;
    private String remarks;
}
