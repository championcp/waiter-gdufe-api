package com.waiterxiaoyy.backandroiddesign;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.waiterxiaoyy.backandroiddesign.mapper")
public class BackandroiddesignApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackandroiddesignApplication.class, args);
    }

}
