package org.gdou.marine.biodiversity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MapPointVO {

    private Long speciesId;
    private String chineseName;
    private String scientificName;
    private BigDecimal lat;
    private BigDecimal lng;
}
