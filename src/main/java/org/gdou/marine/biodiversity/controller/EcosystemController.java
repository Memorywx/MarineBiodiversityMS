package org.gdou.marine.biodiversity.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import org.gdou.marine.biodiversity.common.Result;
import org.gdou.marine.biodiversity.dto.EcosystemDTO;
import org.gdou.marine.biodiversity.service.EcosystemService;
import org.gdou.marine.biodiversity.vo.EcosystemVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecosystems")
public class EcosystemController {

    private final EcosystemService ecosystemService;

    public EcosystemController(EcosystemService ecosystemService) {
        this.ecosystemService = ecosystemService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("创建生态系统")
    public Result<Void> create(@Valid @RequestBody EcosystemDTO dto) {
        ecosystemService.createEcosystem(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESEARCHER')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("更新生态系统")
    public Result<Void> update(@PathVariable Long id, @RequestBody EcosystemDTO dto) {
        ecosystemService.updateEcosystem(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("删除生态系统")
    public Result<Void> delete(@PathVariable Long id) {
        ecosystemService.deleteEcosystem(id);
        return Result.success();
    }

    @GetMapping
    public Result<IPage<EcosystemVO>> list(@RequestParam(required = false) String keyword,
                                            @RequestParam(defaultValue = "1") long current,
                                            @RequestParam(defaultValue = "10") long size) {
        return Result.success(ecosystemService.pageEcosystems(keyword, current, size));
    }

    @GetMapping("/{id}")
    public Result<EcosystemVO> getById(@PathVariable Long id) {
        return Result.success(ecosystemService.getEcosystem(id));
    }

    @GetMapping("/all")
    public Result<List<EcosystemVO>> all() {
        return Result.success(ecosystemService.listAllSimple());
    }
}
