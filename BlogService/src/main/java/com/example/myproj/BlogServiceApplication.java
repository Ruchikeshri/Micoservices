package com.example.myproj;

//import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.util.logging.Logger;

import static java.util.logging.Logger.*;

@EnableEurekaClient
@EnableSwagger2
@Configuration
@SpringBootApplication
@RefreshScope
public class BlogServiceApplication {

	@LoadBalanced
	@Bean
	public RestTemplate getTemplate()
	{
		return  new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(BlogServiceApplication.class, args);
	}

}
