package org.gdou.marine.biodiversity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("species_image")
public class SpeciesImage extends BaseEntity {

    private Long speciesId;
    private String imageUrl;
    private Integer sortOrder;
}
