package org.gdou.marine.biodiversity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.gdou.marine.biodiversity.dto.EcosystemDTO;
import org.gdou.marine.biodiversity.entity.Ecosystem;
import org.gdou.marine.biodiversity.vo.EcosystemVO;

import java.util.List;

public interface EcosystemService extends IService<Ecosystem> {

    void createEcosystem(EcosystemDTO dto);

    void updateEcosystem(Long id, EcosystemDTO dto);

    void deleteEcosystem(Long id);

    IPage<EcosystemVO> pageEcosystems(String keyword, long current, long size);

    EcosystemVO getEcosystem(Long id);

    List<EcosystemVO> listAllSimple();
}
