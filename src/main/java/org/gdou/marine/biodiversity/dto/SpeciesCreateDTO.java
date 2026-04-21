package org.gdou.marine.biodiversity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SpeciesCreateDTO {

    @NotBlank(message = "中文名不能为空")
    private String chineseName;

    @NotBlank(message = "学名不能为空")
    private String scientificName;

    private String phylum;
    private String className;
    private String orderName;
    private String family;
    private String genus;
    private String species;
    private String morphologicalFeatures;
    private String livingHabits;
    private String distribution;
    private BigDecimal distributionLat;
    private BigDecimal distributionLng;
    private String protectionLevel;
    private String iucnStatus;
    private String videoUrl;
    private String references;
    private Integer isPublic;
    private List<String> images;
}
