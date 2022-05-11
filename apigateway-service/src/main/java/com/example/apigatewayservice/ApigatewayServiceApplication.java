package com.example.apigatewayservice;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;
//import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableEurekaClient
//@RestController

public class ApigatewayServiceApplication {
//Logger log= (Logger) LoggerFactory.getLogger(ApigatewayServiceApplication.class);
//
//		@GetMapping("/message")
//		public String get()
//		{
//			log.info("logging from message method");
//			return "springboot-admin-client";
//		}


	public static void main(String[] args) {
		SpringApplication.run(ApigatewayServiceApplication.class, args);
	}

}
