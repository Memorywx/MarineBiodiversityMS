package org.gdou.marine.biodiversity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.gdou.marine.biodiversity.entity.Ecosystem;
import org.gdou.marine.biodiversity.entity.Observation;
import org.gdou.marine.biodiversity.entity.ObservationSpecies;
import org.gdou.marine.biodiversity.entity.Species;
import org.gdou.marine.biodiversity.mapper.EcosystemMapper;
import org.gdou.marine.biodiversity.mapper.ObservationMapper;
import org.gdou.marine.biodiversity.mapper.ObservationSpeciesMapper;
import org.gdou.marine.biodiversity.mapper.SpeciesMapper;
import org.gdou.marine.biodiversity.mapper.UserMapper;
import org.gdou.marine.biodiversity.service.DashboardService;
import org.gdou.marine.biodiversity.vo.DashboardStatsVO;
import org.gdou.marine.biodiversity.vo.MapPointVO;
import org.gdou.marine.biodiversity.vo.ObservationMapPointVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final SpeciesMapper speciesMapper;
    private final ObservationMapper observationMapper;
    private final EcosystemMapper ecosystemMapper;
    private final ObservationSpeciesMapper observationSpeciesMapper;
    private final UserMapper userMapper;

    public DashboardServiceImpl(SpeciesMapper speciesMapper,
                                 ObservationMapper observationMapper,
                                 EcosystemMapper ecosystemMapper,
                                 ObservationSpeciesMapper observationSpeciesMapper,
                                 UserMapper userMapper) {
        this.speciesMapper = speciesMapper;
        this.observationMapper = observationMapper;
        this.ecosystemMapper = ecosystemMapper;
        this.observationSpeciesMapper = observationSpeciesMapper;
        this.userMapper = userMapper;
    }

    @Override
    public DashboardStatsVO getStats() {
        DashboardStatsVO vo = new DashboardStatsVO();
        vo.setTotalSpecies(speciesMapper.selectCount(null));
        vo.setTotalObservations(observationMapper.selectCount(null));
        vo.setTotalEcosystems(ecosystemMapper.selectCount(null));
        vo.setTotalUsers(userMapper.selectCount(null));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        vo.setMonthlyNewObservations(observationMapper.selectCount(
                new LambdaQueryWrapper<Observation>().ge(Observation::getCreateTime, firstDayOfMonth)));
        return vo;
    }

    @Override
    public List<Map<String, Object>> getSpeciesStats(String type) {
        List<Species> list = speciesMapper.selectList(null);
        Map<String, Long> grouped;
        switch (type) {
            case "taxonomy" -> grouped = list.stream()
                    .filter(s -> s.getPhylum() != null)
                    .collect(Collectors.groupingBy(Species::getPhylum, Collectors.counting()));
            case "protection" -> grouped = list.stream()
                    .filter(s -> s.getProtectionLevel() != null)
                    .collect(Collectors.groupingBy(Species::getProtectionLevel, Collectors.counting()));
            case "iucn" -> grouped = list.stream()
                    .filter(s -> s.getIucnStatus() != null)
                    .collect(Collectors.groupingBy(Species::getIucnStatus, Collectors.counting()));
            default -> throw new IllegalArgumentException("不支持的统计类型");
        }
        List<Map<String, Object>> result = new ArrayList<>();
        grouped.forEach((k, v) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", k);
            map.put("value", v);
            result.add(map);
        });
        return result;
    }

    @Override
    public Map<String, Object> getObservationStats(String type) {
        List<Observation> list = observationMapper.selectList(null);
        Map<String, Object> result = new HashMap<>();

        switch (type) {
            case "ecosystem" -> {
                Map<Long, String> ecoMap = ecosystemMapper.selectList(null).stream()
                        .collect(Collectors.toMap(Ecosystem::getId, Ecosystem::getName));
                Map<String, Long> grouped = list.stream()
                        .filter(o -> o.getEcosystemId() != null)
                        .collect(Collectors.groupingBy(o -> ecoMap.getOrDefault(o.getEcosystemId(), "未知"), Collectors.counting()));
                List<Map<String, Object>> data = new ArrayList<>();
                grouped.forEach((k, v) -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", k);
                    m.put("value", v);
                    data.add(m);
                });
                result.put("data", data);
            }
            case "timeline" -> {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
                Map<String, Long> grouped = list.stream()
                        .filter(o -> o.getObservationTime() != null)
                        .collect(Collectors.groupingBy(o -> o.getObservationTime().format(fmt), TreeMap::new, Collectors.counting()));
                result.put("xAxis", new ArrayList<>(grouped.keySet()));
                result.put("series", new ArrayList<>(grouped.values()));
            }
            case "observer" -> {
                Map<String, Long> grouped = list.stream()
                        .filter(o -> o.getObserver() != null)
                        .flatMap(o -> Arrays.stream(o.getObserver().split("[，,、]")))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
                List<Map<String, Object>> data = new ArrayList<>();
                grouped.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .forEach(e -> {
                            Map<String, Object> m = new HashMap<>();
                            m.put("name", e.getKey());
                            m.put("value", e.getValue());
                            data.add(m);
                        });
                result.put("data", data);
            }
            default -> throw new IllegalArgumentException("不支持的统计类型");
        }
        return result;
    }

    @Override
    public List<MapPointVO> getSpeciesDistribution(Long speciesId) {
        LambdaQueryWrapper<Species> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(Species::getDistributionLat).isNotNull(Species::getDistributionLng);
        if (speciesId != null) {
            wrapper.eq(Species::getId, speciesId);
        }
        List<Species> list = speciesMapper.selectList(wrapper);
        return list.stream().map(s -> {
            MapPointVO vo = new MapPointVO();
            vo.setSpeciesId(s.getId());
            vo.setChineseName(s.getChineseName());
            vo.setScientificName(s.getScientificName());
            vo.setLat(s.getDistributionLat());
            vo.setLng(s.getDistributionLng());
            return vo;
        }).toList();
    }

    @Override
    public List<ObservationMapPointVO> getObservationPoints(Long ecosystemId, String startTime, String endTime) {
        LambdaQueryWrapper<Observation> wrapper = new LambdaQueryWrapper<>();
        if (ecosystemId != null) {
            wrapper.eq(Observation::getEcosystemId, ecosystemId);
        }
        if (startTime != null) {
            wrapper.ge(Observation::getObservationTime, LocalDateTime.parse(startTime + "T00:00:00"));
        }
        if (endTime != null) {
            wrapper.le(Observation::getObservationTime, LocalDateTime.parse(endTime + "T23:59:59"));
        }
        List<Observation> list = observationMapper.selectList(wrapper);
        Map<Long, String> ecoMap = ecosystemMapper.selectList(null).stream()
                .collect(Collectors.toMap(Ecosystem::getId, Ecosystem::getName));

        return list.stream().map(o -> {
            ObservationMapPointVO vo = new ObservationMapPointVO();
            vo.setObservationId(o.getId());
            vo.setLat(o.getLatitude());
            vo.setLng(o.getLongitude());
            vo.setLocationName(o.getLocationName());
            vo.setObservationTime(o.getObservationTime());
            vo.setEcosystemName(ecoMap.getOrDefault(o.getEcosystemId(), "未知"));
            long count = observationSpeciesMapper.selectCount(
                    new LambdaQueryWrapper<ObservationSpecies>().eq(ObservationSpecies::getObservationId, o.getId()));
            vo.setSpeciesCount((int) count);
            return vo;
        }).toList();
    }
}
