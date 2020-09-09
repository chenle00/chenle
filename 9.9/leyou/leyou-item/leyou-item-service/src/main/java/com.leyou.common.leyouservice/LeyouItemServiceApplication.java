package com.leyou.common.leyouservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.common.leyouservice.mapper")//mapper接口扫描包

public class LeyouItemServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(LeyouItemServiceApplication.class,args);
    }
}
