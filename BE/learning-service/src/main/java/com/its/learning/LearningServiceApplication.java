package com.its.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.its.learning", "com.its.common"})
@EnableFeignClients()
public class LearningServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LearningServiceApplication.class, args);
	}

}
