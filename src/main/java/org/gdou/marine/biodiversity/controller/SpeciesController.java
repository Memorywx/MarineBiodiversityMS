package org.gdou.marine.biodiversity.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import org.gdou.marine.biodiversity.common.Result;
import org.gdou.marine.biodiversity.dto.SpeciesCreateDTO;
import org.gdou.marine.biodiversity.dto.SpeciesQueryDTO;
import org.gdou.marine.biodiversity.security.SecurityUser;
import org.gdou.marine.biodiversity.service.SpeciesService;
import org.gdou.marine.biodiversity.vo.SpeciesDetailVO;
import org.gdou.marine.biodiversity.vo.SpeciesListVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
public class SpeciesController {

    private final SpeciesService speciesService;

    public SpeciesController(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("创建物种")
    public Result<Void> create(@Valid @RequestBody SpeciesCreateDTO dto,
                                @AuthenticationPrincipal SecurityUser user) {
        speciesService.createSpecies(dto, user.getId());
        return Result.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("更新物种")
    public Result<Void> update(@PathVariable Long id, @RequestBody SpeciesCreateDTO dto) {
        speciesService.updateSpecies(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("删除物种")
    public Result<Void> delete(@PathVariable Long id) {
        speciesService.deleteSpecies(id);
        return Result.success();
    }

    @GetMapping
    public Result<IPage<SpeciesListVO>> list(SpeciesQueryDTO dto,
                                              @AuthenticationPrincipal SecurityUser user) {
        return Result.success(speciesService.pageSpecies(dto, user != null ? user.getRole() : null));
    }

    @GetMapping("/{id}")
    public Result<SpeciesDetailVO> getById(@PathVariable Long id,
                                            @AuthenticationPrincipal SecurityUser user) {
        return Result.success(speciesService.getSpeciesDetail(id, user != null ? user.getRole() : null));
    }

    @GetMapping("/taxonomy")
    public Result<List<String>> taxonomy(@RequestParam String level,
                                          @RequestParam(required = false) String parent) {
        return Result.success(speciesService.getTaxonomyList(level, parent));
    }
}
