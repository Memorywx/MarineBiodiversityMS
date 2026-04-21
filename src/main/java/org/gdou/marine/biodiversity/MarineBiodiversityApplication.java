package org.gdou.marine.biodiversity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.gdou.marine.biodiversity.mapper")
public class MarineBiodiversityApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarineBiodiversityApplication.class, args);
    }
}
