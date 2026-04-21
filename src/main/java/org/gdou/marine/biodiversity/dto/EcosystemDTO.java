package org.gdou.marine.biodiversity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EcosystemDTO {

    @NotBlank(message = "名称不能为空")
    private String name;
    private String type;
    private String description;
    private String geoRange;
    private String environmentFeatures;
}
