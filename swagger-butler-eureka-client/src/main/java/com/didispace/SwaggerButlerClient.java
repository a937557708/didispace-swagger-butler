package com.didispace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.didispace.swagger.butler.EnableSwaggerButler;

/**
 * Created by 程序猿DD/翟永超 on 2018/5/24.
 * <p>
 * Blog: http://blog.didispace.com/ Github: https://github.com/dyc87112/
 */

@EnableSwaggerButler
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
@ComponentScan({ "com.didispace" })
//开启缓存功能[redis]
@EnableCaching
public class SwaggerButlerClient {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(SwaggerButlerClient.class);

		Map map = new HashMap();

		map.put("eureka.client.service-url.defaultZone", "http://127.0.0.1:8510/eureka");

		springApplication.setDefaultProperties(map);// 设置命令行参数
		// 禁止命令行设置参数
		// springApplication.setAddCommandLineProperties(false);

		// String[] args1=
		// {"eureka.client.service-url.defaultZone=http://127.0.0.1:85101/eureka"};
		springApplication.run(args);
	}

}
