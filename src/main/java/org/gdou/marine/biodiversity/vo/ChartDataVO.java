package org.gdou.marine.biodiversity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ChartDataVO {

    private List<String> xAxis;
    private List<?> series;
}
