package com.hotel.habitaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hotel")
public class HabitacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitacionesApplication.class, args);
    }
}