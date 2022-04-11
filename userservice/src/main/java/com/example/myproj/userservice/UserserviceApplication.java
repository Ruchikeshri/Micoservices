package com.example.myproj.userservice;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableEurekaClient
@EnableSwagger2
@SpringBootApplication
public class UserserviceApplication {
    @LoadBalanced
    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }

//    @Value("${spring.rabbitmq.host}")
//    String host;
//    @Value("${spring.rabbitmq.username}")
//    String username;
//    @Value("${spring.rabbitmq.password}")
//    String password;

    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);
    }



}
