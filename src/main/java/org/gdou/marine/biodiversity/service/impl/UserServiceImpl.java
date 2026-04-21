package org.gdou.marine.biodiversity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gdou.marine.biodiversity.common.exception.BusinessException;
import org.gdou.marine.biodiversity.dto.LoginDTO;
import org.gdou.marine.biodiversity.dto.RegisterDTO;
import org.gdou.marine.biodiversity.dto.UserCreateDTO;
import org.gdou.marine.biodiversity.dto.UserQueryDTO;
import org.gdou.marine.biodiversity.entity.User;
import org.gdou.marine.biodiversity.mapper.UserMapper;
import org.gdou.marine.biodiversity.security.JwtUtil;
import org.gdou.marine.biodiversity.service.UserService;
import org.gdou.marine.biodiversity.vo.LoginVO;
import org.gdou.marine.biodiversity.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        User user = baseMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null || user.getStatus() != 1) {
            throw new BusinessException(401, "账号未激活或已被禁用");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setExpiresIn(86400);
        vo.setUser(convertToVO(user));
        return vo;
    }

    @Override
    public void register(RegisterDTO dto) {
        if (baseMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRealName(dto.getRealName());
        user.setRole(dto.getRole());
        user.setStatus(0);
        baseMapper.insert(user);
    }

    @Override
    public IPage<UserVO> pageUsers(UserQueryDTO dto) {
        Page<User> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, dto.getKeyword())
                    .or().like(User::getRealName, dto.getKeyword()));
        }
        if (dto.getRole() != null) {
            wrapper.eq(User::getRole, dto.getRole());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(User::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(User::getCreateTime);
        IPage<User> userPage = baseMapper.selectPage(page, wrapper);
        return userPage.convert(this::convertToVO);
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public void createUser(UserCreateDTO dto) {
        if (baseMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(1);
        baseMapper.insert(user);
    }

    @Override
    public void updateUser(Long id, UserCreateDTO dto) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setRealName(dto.getRealName());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        baseMapper.updateById(user);
    }

    @Override
    public void updateRoleStatus(Long id, Integer role, Integer status) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        if (role != null) {
            user.setRole(role);
        }
        if (status != null) {
            user.setStatus(status);
        }
        baseMapper.updateById(user);
    }

    @Override
    public void deleteUser(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void approveUser(Long id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setStatus(1);
        baseMapper.updateById(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
