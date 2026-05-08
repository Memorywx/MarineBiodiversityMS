package org.gdou.marine.biodiversity.vo;

import lombok.Data;

@Data
public class DashboardStatsVO {

    private long totalSpecies;
    private long totalObservations;
    private long totalEcosystems;
    private long totalUsers;
    private long monthlyNewObservations;
}
