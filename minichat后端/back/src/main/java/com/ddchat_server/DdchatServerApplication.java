package com.ddchat_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.ddchat_server")
@EnableAsync
public class DdchatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdchatServerApplication.class, args);
    }

}
