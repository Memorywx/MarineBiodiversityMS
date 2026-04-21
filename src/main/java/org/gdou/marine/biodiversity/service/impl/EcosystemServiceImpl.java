package org.gdou.marine.biodiversity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gdou.marine.biodiversity.common.exception.BusinessException;
import org.gdou.marine.biodiversity.dto.EcosystemDTO;
import org.gdou.marine.biodiversity.entity.Ecosystem;
import org.gdou.marine.biodiversity.entity.Observation;
import org.gdou.marine.biodiversity.mapper.EcosystemMapper;
import org.gdou.marine.biodiversity.mapper.ObservationMapper;
import org.gdou.marine.biodiversity.service.EcosystemService;
import org.gdou.marine.biodiversity.vo.EcosystemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EcosystemServiceImpl extends ServiceImpl<EcosystemMapper, Ecosystem> implements EcosystemService {

    private final ObservationMapper observationMapper;

    public EcosystemServiceImpl(ObservationMapper observationMapper) {
        this.observationMapper = observationMapper;
    }

    @Override
    public void createEcosystem(EcosystemDTO dto) {
        if (baseMapper.selectCount(new LambdaQueryWrapper<Ecosystem>().eq(Ecosystem::getName, dto.getName())) > 0) {
            throw new BusinessException("生态系统名称已存在");
        }
        Ecosystem ecosystem = new Ecosystem();
        BeanUtils.copyProperties(dto, ecosystem);
        baseMapper.insert(ecosystem);
    }

    @Override
    public void updateEcosystem(Long id, EcosystemDTO dto) {
        Ecosystem ecosystem = baseMapper.selectById(id);
        if (ecosystem == null) {
            throw new BusinessException(404, "生态系统不存在");
        }
        BeanUtils.copyProperties(dto, ecosystem);
        ecosystem.setId(id);
        baseMapper.updateById(ecosystem);
    }

    @Override
    public void deleteEcosystem(Long id) {
        long count = observationMapper.selectCount(
                new LambdaQueryWrapper<Observation>().eq(Observation::getEcosystemId, id));
        if (count > 0) {
            throw new BusinessException("该生态系统下存在观测记录，无法删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public IPage<EcosystemVO> pageEcosystems(String keyword, long current, long size) {
        Page<Ecosystem> page = new Page<>(current, size);
        LambdaQueryWrapper<Ecosystem> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Ecosystem::getName, keyword);
        }
        wrapper.orderByDesc(Ecosystem::getCreateTime);
        return baseMapper.selectPage(page, wrapper).convert(this::convertToVO);
    }

    @Override
    public EcosystemVO getEcosystem(Long id) {
        Ecosystem ecosystem = baseMapper.selectById(id);
        if (ecosystem == null) {
            throw new BusinessException(404, "生态系统不存在");
        }
        return convertToVO(ecosystem);
    }

    @Override
    public List<EcosystemVO> listAllSimple() {
        return baseMapper.selectAllSimple().stream().map(this::convertToVO).toList();
    }

    private EcosystemVO convertToVO(Ecosystem ecosystem) {
        EcosystemVO vo = new EcosystemVO();
        BeanUtils.copyProperties(ecosystem, vo);
        return vo;
    }
}
