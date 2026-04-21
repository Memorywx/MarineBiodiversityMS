package org.gdou.marine.biodiversity.controller;

import org.gdou.marine.biodiversity.common.Result;
import org.gdou.marine.biodiversity.service.DashboardService;
import org.gdou.marine.biodiversity.vo.DashboardStatsVO;
import org.gdou.marine.biodiversity.vo.MapPointVO;
import org.gdou.marine.biodiversity.vo.ObservationMapPointVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public Result<DashboardStatsVO> stats() {
        return Result.success(dashboardService.getStats());
    }

    @GetMapping("/species-stats")
    public Result<List<Map<String, Object>>> speciesStats(@RequestParam String type) {
        return Result.success(dashboardService.getSpeciesStats(type));
    }

    @GetMapping("/observation-stats")
    public Result<Map<String, Object>> observationStats(@RequestParam String type) {
        return Result.success(dashboardService.getObservationStats(type));
    }

    @GetMapping("/species-distribution")
    public Result<List<MapPointVO>> speciesDistribution(@RequestParam(required = false) Long speciesId) {
        return Result.success(dashboardService.getSpeciesDistribution(speciesId));
    }

    @GetMapping("/observation-points")
    public Result<List<ObservationMapPointVO>> observationPoints(
            @RequestParam(required = false) Long ecosystemId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.success(dashboardService.getObservationPoints(ecosystemId, startTime, endTime));
    }
}
