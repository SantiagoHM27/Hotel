package com.hotel.huespedes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.hotel.huespedes", "com.hotel.commons"})
@EnableDiscoveryClient
@EnableFeignClients
public class HuespedesApplication {
    public static void main(String[] args) {
        SpringApplication.run(HuespedesApplication.class, args);
    }
}