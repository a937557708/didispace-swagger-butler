package com.didispace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.didispace.swagger.butler.EnableSwaggerButler;

@EnableDiscoveryClient
@SpringBootApplication
@EnableSwaggerButler
public class SwaggerConsulClient {
	public static void main(String[] args) {
		SpringApplication.run(SwaggerConsulClient.class, args);
	}
}
