package org.gdou.marine.biodiversity.controller;

import jakarta.validation.Valid;
import org.gdou.marine.biodiversity.common.Result;
import org.gdou.marine.biodiversity.dto.LoginDTO;
import org.gdou.marine.biodiversity.dto.RegisterDTO;
import org.gdou.marine.biodiversity.entity.User;
import org.gdou.marine.biodiversity.mapper.UserMapper;
import org.gdou.marine.biodiversity.security.SecurityUser;
import org.gdou.marine.biodiversity.service.UserService;
import org.gdou.marine.biodiversity.vo.LoginVO;
import org.gdou.marine.biodiversity.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("用户登录")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(userService.login(dto));
    }

    @PostMapping("/register")
    @org.gdou.marine.biodiversity.common.annotation.LogOperation("用户注册")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @GetMapping("/me")
    public Result<UserVO> me(@AuthenticationPrincipal SecurityUser securityUser) {
        User user = userMapper.selectById(securityUser.getId());
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return Result.success(vo);
    }
}
