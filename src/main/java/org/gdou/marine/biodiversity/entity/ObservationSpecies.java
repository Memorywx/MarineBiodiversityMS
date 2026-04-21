package org.gdou.marine.biodiversity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("observation_species")
public class ObservationSpecies extends BaseEntity {

    private Long observationId;
    private Long speciesId;
    private Integer estimatedQuantity;
    private String behavior;
    private String remarks;
}
