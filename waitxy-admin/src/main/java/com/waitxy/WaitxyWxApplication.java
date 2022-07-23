package com.waitxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author waitxy
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WaitxyWxApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(WaitxyWxApplication.class, args);
        System.out.println("waitxy微信管理系统启动成功");
    }
}
