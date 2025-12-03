package com.its.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.its.iam", "com.its.common"})
public class IAMServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IAMServiceApplication.class, args);
	}

}
