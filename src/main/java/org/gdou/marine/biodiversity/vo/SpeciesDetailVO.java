package org.gdou.marine.biodiversity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SpeciesDetailVO {

    private Long id;
    private String chineseName;
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
    private Long createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
