package org.gdou.marine.biodiversity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpeciesListVO {

    private Long id;
    private String chineseName;
    private String scientificName;
    private String phylum;
    private String className;
    private String protectionLevel;
    private String iucnStatus;
    private String coverImage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
