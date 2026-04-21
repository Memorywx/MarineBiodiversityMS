package org.gdou.marine.biodiversity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gdou.marine.biodiversity.common.exception.BusinessException;
import org.gdou.marine.biodiversity.dto.SpeciesCreateDTO;
import org.gdou.marine.biodiversity.dto.SpeciesQueryDTO;
import org.gdou.marine.biodiversity.entity.Species;
import org.gdou.marine.biodiversity.entity.SpeciesImage;
import org.gdou.marine.biodiversity.mapper.ObservationSpeciesMapper;
import org.gdou.marine.biodiversity.mapper.SpeciesImageMapper;
import org.gdou.marine.biodiversity.mapper.SpeciesMapper;
import org.gdou.marine.biodiversity.service.SpeciesService;
import org.gdou.marine.biodiversity.vo.SpeciesDetailVO;
import org.gdou.marine.biodiversity.vo.SpeciesListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpeciesServiceImpl extends ServiceImpl<SpeciesMapper, Species> implements SpeciesService {

    private final SpeciesImageMapper speciesImageMapper;
    private final ObservationSpeciesMapper observationSpeciesMapper;

    public SpeciesServiceImpl(SpeciesImageMapper speciesImageMapper, ObservationSpeciesMapper observationSpeciesMapper) {
        this.speciesImageMapper = speciesImageMapper;
        this.observationSpeciesMapper = observationSpeciesMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSpecies(SpeciesCreateDTO dto, Long userId) {
        if (baseMapper.selectCount(new LambdaQueryWrapper<Species>().eq(Species::getScientificName, dto.getScientificName())) > 0) {
            throw new BusinessException("学名已存在");
        }
        Species species = new Species();
        BeanUtils.copyProperties(dto, species);
        species.setCreateBy(userId);
        if (species.getIsPublic() == null) {
            species.setIsPublic(1);
        }
        baseMapper.insert(species);

        saveImages(species.getId(), dto.getImages());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpecies(Long id, SpeciesCreateDTO dto) {
        Species species = baseMapper.selectById(id);
        if (species == null) {
            throw new BusinessException(404, "物种不存在");
        }
        BeanUtils.copyProperties(dto, species);
        species.setId(id);
        baseMapper.updateById(species);

        speciesImageMapper.delete(new LambdaQueryWrapper<SpeciesImage>().eq(SpeciesImage::getSpeciesId, id));
        saveImages(id, dto.getImages());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpecies(Long id) {
        if (observationSpeciesMapper.selectCount(
                new LambdaQueryWrapper<org.gdou.marine.biodiversity.entity.ObservationSpecies>().eq(org.gdou.marine.biodiversity.entity.ObservationSpecies::getSpeciesId, id)) > 0) {
            throw new BusinessException("该物种已被观测记录关联，请先解除关联");
        }
        speciesImageMapper.delete(new LambdaQueryWrapper<SpeciesImage>().eq(SpeciesImage::getSpeciesId, id));
        baseMapper.deleteById(id);
    }

    @Override
    public IPage<SpeciesListVO> pageSpecies(SpeciesQueryDTO dto, Integer userRole) {
        Page<Species> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<Species> wrapper = new LambdaQueryWrapper<>();
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like(Species::getChineseName, dto.getKeyword())
                    .or().like(Species::getScientificName, dto.getKeyword()));
        }
        if (dto.getPhylum() != null && !dto.getPhylum().isEmpty()) {
            wrapper.eq(Species::getPhylum, dto.getPhylum());
        }
        if (dto.getClassName() != null && !dto.getClassName().isEmpty()) {
            wrapper.eq(Species::getClassName, dto.getClassName());
        }
        if (dto.getOrderName() != null && !dto.getOrderName().isEmpty()) {
            wrapper.eq(Species::getOrderName, dto.getOrderName());
        }
        if (dto.getFamily() != null && !dto.getFamily().isEmpty()) {
            wrapper.eq(Species::getFamily, dto.getFamily());
        }
        if (dto.getProtectionLevel() != null && !dto.getProtectionLevel().isEmpty()) {
            wrapper.eq(Species::getProtectionLevel, dto.getProtectionLevel());
        }
        if (dto.getIucnStatus() != null && !dto.getIucnStatus().isEmpty()) {
            wrapper.eq(Species::getIucnStatus, dto.getIucnStatus());
        }
        if (userRole != null && userRole == 3) {
            wrapper.eq(Species::getIsPublic, 1);
        }
        wrapper.orderByDesc(Species::getCreateTime);

        IPage<Species> speciesPage = baseMapper.selectPage(page, wrapper);
        return speciesPage.convert(this::convertToListVO);
    }

    @Override
    public SpeciesDetailVO getSpeciesDetail(Long id, Integer userRole) {
        Species species = baseMapper.selectById(id);
        if (species == null) {
            throw new BusinessException(404, "物种不存在");
        }
        if (userRole != null && userRole == 3 && species.getIsPublic() != 1) {
            throw new BusinessException(403, "无权限查看该物种");
        }
        SpeciesDetailVO vo = new SpeciesDetailVO();
        BeanUtils.copyProperties(species, vo);
        List<SpeciesImage> images = speciesImageMapper.selectList(
                new LambdaQueryWrapper<SpeciesImage>().eq(SpeciesImage::getSpeciesId, id).orderByAsc(SpeciesImage::getSortOrder));
        vo.setImages(images.stream().map(SpeciesImage::getImageUrl).toList());
        return vo;
    }

    @Override
    public List<String> getTaxonomyList(String level, String parent) {
        String column = switch (level) {
            case "phylum" -> "phylum";
            case "class" -> "class_name";
            case "order" -> "order_name";
            case "family" -> "family";
            default -> throw new BusinessException("不支持的分类层级");
        };
        // 简单的级联：如果传了parent，按上级筛选（这里简化，只返回对应列的不重复值）
        return baseMapper.selectDistinctColumn(column);
    }

    private void saveImages(Long speciesId, List<String> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        for (int i = 0; i < images.size(); i++) {
            SpeciesImage img = new SpeciesImage();
            img.setSpeciesId(speciesId);
            img.setImageUrl(images.get(i));
            img.setSortOrder(i);
            speciesImageMapper.insert(img);
        }
    }

    private SpeciesListVO convertToListVO(Species species) {
        SpeciesListVO vo = new SpeciesListVO();
        BeanUtils.copyProperties(species, vo);
        List<SpeciesImage> images = speciesImageMapper.selectList(
                new LambdaQueryWrapper<SpeciesImage>()
                        .eq(SpeciesImage::getSpeciesId, species.getId())
                        .orderByAsc(SpeciesImage::getSortOrder)
                        .last("LIMIT 1"));
        if (!images.isEmpty()) {
            vo.setCoverImage(images.get(0).getImageUrl());
        }
        return vo;
    }
}
