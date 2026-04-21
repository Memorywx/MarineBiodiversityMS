package org.gdou.marine.biodiversity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gdou.marine.biodiversity.common.exception.BusinessException;
import org.gdou.marine.biodiversity.dto.ObservationCreateDTO;
import org.gdou.marine.biodiversity.dto.ObservationQueryDTO;
import org.gdou.marine.biodiversity.dto.ObservationSpeciesDTO;
import org.gdou.marine.biodiversity.entity.Ecosystem;
import org.gdou.marine.biodiversity.entity.Observation;
import org.gdou.marine.biodiversity.entity.ObservationSpecies;
import org.gdou.marine.biodiversity.entity.Species;
import org.gdou.marine.biodiversity.mapper.EcosystemMapper;
import org.gdou.marine.biodiversity.mapper.ObservationMapper;
import org.gdou.marine.biodiversity.mapper.ObservationSpeciesMapper;
import org.gdou.marine.biodiversity.mapper.SpeciesMapper;
import org.gdou.marine.biodiversity.service.ObservationService;
import org.gdou.marine.biodiversity.vo.ObservationDetailVO;
import org.gdou.marine.biodiversity.vo.ObservationListVO;
import org.gdou.marine.biodiversity.vo.ObservationSpeciesVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ObservationServiceImpl extends ServiceImpl<ObservationMapper, Observation> implements ObservationService {

    private final ObservationSpeciesMapper observationSpeciesMapper;
    private final EcosystemMapper ecosystemMapper;
    private final SpeciesMapper speciesMapper;

    public ObservationServiceImpl(ObservationSpeciesMapper observationSpeciesMapper,
                                   EcosystemMapper ecosystemMapper,
                                   SpeciesMapper speciesMapper) {
        this.observationSpeciesMapper = observationSpeciesMapper;
        this.ecosystemMapper = ecosystemMapper;
        this.speciesMapper = speciesMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createObservation(ObservationCreateDTO dto, Long userId) {
        Observation observation = new Observation();
        BeanUtils.copyProperties(dto, observation);
        observation.setCreateBy(userId);
        observation.setObserverUserId(userId);
        baseMapper.insert(observation);

        saveObservationSpecies(observation.getId(), dto.getSpeciesList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateObservation(Long id, ObservationCreateDTO dto) {
        Observation observation = baseMapper.selectById(id);
        if (observation == null) {
            throw new BusinessException(404, "观测记录不存在");
        }
        BeanUtils.copyProperties(dto, observation);
        observation.setId(id);
        baseMapper.updateById(observation);

        observationSpeciesMapper.delete(
                new LambdaQueryWrapper<ObservationSpecies>().eq(ObservationSpecies::getObservationId, id));
        saveObservationSpecies(id, dto.getSpeciesList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteObservation(Long id) {
        Observation observation = baseMapper.selectById(id);
        if (observation == null) {
            throw new BusinessException(404, "观测记录不存在");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public IPage<ObservationListVO> pageObservations(ObservationQueryDTO dto) {
        Page<Observation> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<Observation> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(dto.getKeyword())) {
            wrapper.like(Observation::getLocationName, dto.getKeyword());
        }
        if (dto.getEcosystemId() != null) {
            wrapper.eq(Observation::getEcosystemId, dto.getEcosystemId());
        }
        if (StringUtils.hasText(dto.getStartTime())) {
            LocalDateTime start = LocalDateTime.parse(dto.getStartTime() + "T00:00:00");
            wrapper.ge(Observation::getObservationTime, start);
        }
        if (StringUtils.hasText(dto.getEndTime())) {
            LocalDateTime end = LocalDateTime.parse(dto.getEndTime() + "T23:59:59");
            wrapper.le(Observation::getObservationTime, end);
        }

        wrapper.orderByDesc(Observation::getObservationTime);
        IPage<Observation> observationPage = baseMapper.selectPage(page, wrapper);

        // 如果按物种筛选，需要后过滤（简化处理）
        List<Observation> records = observationPage.getRecords();
        if (dto.getSpeciesId() != null) {
            records = records.stream()
                    .filter(o -> observationSpeciesMapper.selectCount(
                            new LambdaQueryWrapper<ObservationSpecies>()
                                    .eq(ObservationSpecies::getObservationId, o.getId())
                                    .eq(ObservationSpecies::getSpeciesId, dto.getSpeciesId())) > 0)
                    .toList();
        }

        List<ObservationListVO> voList = records.stream().map(this::convertToListVO).toList();
        Page<ObservationListVO> resultPage = new Page<>(observationPage.getCurrent(), observationPage.getSize(), observationPage.getTotal());
        resultPage.setRecords(voList);
        return resultPage;
    }

    @Override
    public ObservationDetailVO getObservationDetail(Long id) {
        Observation observation = baseMapper.selectById(id);
        if (observation == null) {
            throw new BusinessException(404, "观测记录不存在");
        }
        ObservationDetailVO vo = new ObservationDetailVO();
        BeanUtils.copyProperties(observation, vo);

        if (observation.getEcosystemId() != null) {
            Ecosystem eco = ecosystemMapper.selectById(observation.getEcosystemId());
            if (eco != null) {
                vo.setEcosystemName(eco.getName());
            }
        }

        List<ObservationSpecies> osList = observationSpeciesMapper.selectList(
                new LambdaQueryWrapper<ObservationSpecies>().eq(ObservationSpecies::getObservationId, id));

        List<ObservationSpeciesVO> speciesVOList = osList.stream().map(os -> {
            ObservationSpeciesVO svo = new ObservationSpeciesVO();
            svo.setSpeciesId(os.getSpeciesId());
            svo.setEstimatedQuantity(os.getEstimatedQuantity());
            svo.setBehavior(os.getBehavior());
            svo.setRemarks(os.getRemarks());
            Species species = speciesMapper.selectById(os.getSpeciesId());
            if (species != null) {
                svo.setChineseName(species.getChineseName());
                svo.setScientificName(species.getScientificName());
            }
            return svo;
        }).toList();
        vo.setSpeciesList(speciesVOList);
        return vo;
    }

    private void saveObservationSpecies(Long observationId, List<ObservationSpeciesDTO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (ObservationSpeciesDTO item : list) {
            ObservationSpecies os = new ObservationSpecies();
            os.setObservationId(observationId);
            os.setSpeciesId(item.getSpeciesId());
            os.setEstimatedQuantity(item.getEstimatedQuantity());
            os.setBehavior(item.getBehavior());
            os.setRemarks(item.getRemarks());
            observationSpeciesMapper.insert(os);
        }
    }

    private ObservationListVO convertToListVO(Observation observation) {
        ObservationListVO vo = new ObservationListVO();
        BeanUtils.copyProperties(observation, vo);
        if (observation.getEcosystemId() != null) {
            Ecosystem eco = ecosystemMapper.selectById(observation.getEcosystemId());
            if (eco != null) {
                vo.setEcosystemName(eco.getName());
            }
        }
        long count = observationSpeciesMapper.selectCount(
                new LambdaQueryWrapper<ObservationSpecies>().eq(ObservationSpecies::getObservationId, observation.getId()));
        vo.setSpeciesCount((int) count);
        return vo;
    }
}
