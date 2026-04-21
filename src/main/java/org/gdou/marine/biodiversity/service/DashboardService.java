package org.gdou.marine.biodiversity.service;

import org.gdou.marine.biodiversity.vo.*;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    DashboardStatsVO getStats();

    List<Map<String, Object>> getSpeciesStats(String type);

    Map<String, Object> getObservationStats(String type);

    List<MapPointVO> getSpeciesDistribution(Long speciesId);

    List<ObservationMapPointVO> getObservationPoints(Long ecosystemId, String startTime, String endTime);
}
