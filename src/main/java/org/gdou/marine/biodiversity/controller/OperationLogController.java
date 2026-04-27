package org.gdou.marine.biodiversity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.gdou.marine.biodiversity.common.Result;
import org.gdou.marine.biodiversity.entity.OperationLog;
import org.gdou.marine.biodiversity.mapper.OperationLogMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operation-logs")
public class OperationLogController {

    private final OperationLogMapper operationLogMapper;

    public OperationLogController(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    @GetMapping
    public Result<IPage<OperationLog>> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operation,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "20") long size) {
        Page<OperationLog> page = new Page<>(current, size);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(OperationLog::getCreateTime);
        if (username != null && !username.isBlank()) {
            wrapper.like(OperationLog::getUsername, username);
        }
        if (operation != null && !operation.isBlank()) {
            wrapper.like(OperationLog::getOperation, operation);
        }
        return Result.success(operationLogMapper.selectPage(page, wrapper));
    }
}
