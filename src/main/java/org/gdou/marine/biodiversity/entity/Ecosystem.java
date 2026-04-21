package org.gdou.marine.biodiversity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecosystem")
public class Ecosystem extends BaseEntity {

    private String name;
    private String type;
    private String description;
    private String geoRange;
    private String environmentFeatures;
}
