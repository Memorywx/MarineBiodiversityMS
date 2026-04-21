package org.gdou.marine.biodiversity.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.gdou.marine.biodiversity.common.Result;
import org.gdou.marine.biodiversity.dto.UserCreateDTO;
import org.gdou.marine.biodiversity.dto.UserQueryDTO;
import org.gdou.marine.biodiversity.service.UserService;
import org.gdou.marine.biodiversity.vo.UserVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<UserVO>> list(UserQueryDTO dto) {
        return Result.success(userService.pageUsers(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).username == authentication.name")
    public Result<UserVO> getById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("创建用户")
    public Result<Void> create(@RequestBody UserCreateDTO dto) {
        userService.createUser(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("更新用户")
    public Result<Void> update(@PathVariable Long id, @RequestBody UserCreateDTO dto) {
        userService.updateUser(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/role-status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateRoleStatus(@PathVariable Long id,
                                          @RequestParam Integer role,
                                          @RequestParam Integer status) {
        userService.updateRoleStatus(id, role, status);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("删除用户")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("审核用户")
    public Result<Void> approve(@PathVariable Long id) {
        userService.approveUser(id);
        return Result.success();
    }
}
