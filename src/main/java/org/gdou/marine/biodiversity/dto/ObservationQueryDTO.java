package org.gdou.marine.biodiversity.dto;

import lombok.Data;

@Data
public class ObservationQueryDTO {

    private String keyword;
    private Long ecosystemId;
    private String startTime;
    private String endTime;
    private Long speciesId;
    private long current = 1;
    private long size = 10;
}
