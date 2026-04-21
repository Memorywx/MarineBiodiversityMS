package org.gdou.marine.biodiversity.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.gdou.marine.biodiversity.entity.User;
import org.gdou.marine.biodiversity.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            long count = userMapper.selectCount(new QueryWrapper<User>());
            System.out.println("[DataInitializer] 当前用户数量: " + count);
            if (count == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRealName("系统管理员");
                admin.setEmail("admin@gdou.edu.cn");
                admin.setRole(0);
                admin.setStatus(1);
                userMapper.insert(admin);

                User researcher = new User();
                researcher.setUsername("researcher");
                researcher.setPassword(passwordEncoder.encode("123456"));
                researcher.setRealName("李教授");
                researcher.setEmail("li@gdou.edu.cn");
                researcher.setRole(1);
                researcher.setStatus(1);
                userMapper.insert(researcher);

                System.out.println("========================================");
                System.out.println("  默认用户已创建：");
                System.out.println("    admin       / 123456  (管理员)");
                System.out.println("    researcher  / 123456  (科研人员)");
                System.out.println("========================================");
            } else {
                System.out.println("[DataInitializer] 用户表已有数据，跳过初始化");
            }
        } catch (Exception e) {
            System.err.println("[DataInitializer] 初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
