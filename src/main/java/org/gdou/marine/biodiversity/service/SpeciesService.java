package org.gdou.marine.biodiversity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.gdou.marine.biodiversity.dto.SpeciesCreateDTO;
import org.gdou.marine.biodiversity.dto.SpeciesQueryDTO;
import org.gdou.marine.biodiversity.entity.Species;
import org.gdou.marine.biodiversity.vo.SpeciesDetailVO;
import org.gdou.marine.biodiversity.vo.SpeciesListVO;

import java.util.List;

public interface SpeciesService extends IService<Species> {

    void createSpecies(SpeciesCreateDTO dto, Long userId);

    void updateSpecies(Long id, SpeciesCreateDTO dto);

    void deleteSpecies(Long id);

    IPage<SpeciesListVO> pageSpecies(SpeciesQueryDTO dto, Integer userRole);

    SpeciesDetailVO getSpeciesDetail(Long id, Integer userRole);

    List<String> getTaxonomyList(String level, String parent);
}
