package org.gdou.marine.biodiversity.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.gdou.marine.biodiversity.entity.User;
import org.gdou.marine.biodiversity.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return new SecurityUser(user.getId(), user.getUsername(), user.getPassword(),
                user.getRole(), user.getStatus());
    }
}
