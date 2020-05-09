package com.zhaoxun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zhaoxun.mapper")
@SpringBootApplication
public class ZookeeperlockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperlockApplication.class, args);
    }

}
