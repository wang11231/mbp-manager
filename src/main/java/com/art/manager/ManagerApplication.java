package com.art.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 入口类
 * @author Administrator
 */
@SpringBootApplication
@MapperScan("com.art.manager.mapper")
public class ManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}
}
