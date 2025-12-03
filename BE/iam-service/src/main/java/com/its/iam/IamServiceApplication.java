package com.its.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.its.user", "com.its.common"})
public class IamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IamServiceApplication.class, args);
	}

}
