package com.didispace.Swagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration // 启动时就要加载
@EnableSwagger2
public class SwaggerConfig {
	private String version;

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("com.didispace.controller")
				.genericModelSubstitutes(DeferredResult.class)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.didispace.controller"))
				.paths(PathSelectors.any())
				.build().apiInfo(apiInfo());

	}
	
	private ApiInfo apiInfo(){
		
		return new ApiInfoBuilder().title("中金安盛科技有限公司")
				.description("这是无聊时写的一个测试API")
				.termsOfServiceUrl("http://www.baidu.com")
				.version("2.0").build();
	}
}
