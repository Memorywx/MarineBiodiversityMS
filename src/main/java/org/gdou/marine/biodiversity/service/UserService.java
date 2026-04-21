package org.gdou.marine.biodiversity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.gdou.marine.biodiversity.dto.LoginDTO;
import org.gdou.marine.biodiversity.dto.RegisterDTO;
import org.gdou.marine.biodiversity.dto.UserCreateDTO;
import org.gdou.marine.biodiversity.dto.UserQueryDTO;
import org.gdou.marine.biodiversity.entity.User;
import org.gdou.marine.biodiversity.vo.LoginVO;
import org.gdou.marine.biodiversity.vo.UserVO;

public interface UserService extends IService<User> {

    LoginVO login(LoginDTO dto);

    void register(RegisterDTO dto);

    IPage<UserVO> pageUsers(UserQueryDTO dto);

    UserVO getUserById(Long id);

    void createUser(UserCreateDTO dto);

    void updateUser(Long id, UserCreateDTO dto);

    void updateRoleStatus(Long id, Integer role, Integer status);

    void deleteUser(Long id);

    void approveUser(Long id);
}
