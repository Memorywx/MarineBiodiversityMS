package org.gdou.marine.biodiversity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.gdou.marine.biodiversity.dto.ObservationCreateDTO;
import org.gdou.marine.biodiversity.dto.ObservationQueryDTO;
import org.gdou.marine.biodiversity.entity.Observation;
import org.gdou.marine.biodiversity.vo.ObservationDetailVO;
import org.gdou.marine.biodiversity.vo.ObservationListVO;

public interface ObservationService extends IService<Observation> {

    void createObservation(ObservationCreateDTO dto, Long userId);

    void updateObservation(Long id, ObservationCreateDTO dto);

    void deleteObservation(Long id);

    IPage<ObservationListVO> pageObservations(ObservationQueryDTO dto);

    ObservationDetailVO getObservationDetail(Long id);
}
