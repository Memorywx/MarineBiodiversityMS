package org.gdou.marine.biodiversity.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import org.gdou.marine.biodiversity.common.Result;
import org.gdou.marine.biodiversity.dto.ObservationCreateDTO;
import org.gdou.marine.biodiversity.dto.ObservationQueryDTO;
import org.gdou.marine.biodiversity.security.SecurityUser;
import org.gdou.marine.biodiversity.service.ObservationService;
import org.gdou.marine.biodiversity.vo.ObservationDetailVO;
import org.gdou.marine.biodiversity.vo.ObservationListVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationService observationService;

    public ObservationController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("创建观测记录")
    public Result<Void> create(@Valid @RequestBody ObservationCreateDTO dto,
                                @AuthenticationPrincipal SecurityUser user) {
        observationService.createObservation(dto, user.getId());
        return Result.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("更新观测记录")
    public Result<Void> update(@PathVariable Long id, @RequestBody ObservationCreateDTO dto) {
        observationService.updateObservation(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("删除观测记录")
    public Result<Void> delete(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return Result.success();
    }

    @GetMapping
    public Result<IPage<ObservationListVO>> list(ObservationQueryDTO dto) {
        return Result.success(observationService.pageObservations(dto));
    }

    @GetMapping("/{id}")
    public Result<ObservationDetailVO> getById(@PathVariable Long id) {
        return Result.success(observationService.getObservationDetail(id));
    }
}
