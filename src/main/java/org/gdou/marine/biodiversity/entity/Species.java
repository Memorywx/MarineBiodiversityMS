package org.gdou.marine.biodiversity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("species")
public class Species extends BaseEntity {

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
    private Long createBy;
}
