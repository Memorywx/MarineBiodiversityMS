package org.gdou.marine.biodiversity.dto;

import lombok.Data;

@Data
public class SpeciesQueryDTO {

    private String keyword;
    private String phylum;
    private String className;
    private String orderName;
    private String family;
    private String protectionLevel;
    private String iucnStatus;
    private long current = 1;
    private long size = 10;
}
