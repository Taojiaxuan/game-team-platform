package com.gameteam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gameteam.mapper")
public class GameTeamApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameTeamApplication.class, args);
    }
}
