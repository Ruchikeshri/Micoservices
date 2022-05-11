package com.example.myproj;

//import org.apache.log4j.PropertyConfigurator;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.catalina.mbeans.MBeanFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static java.util.logging.Logger.*;

@EnableEurekaClient
@EnableSwagger2
//@OpenAPIDefinition(info =@Info(title="Blog Service"))
@Configuration
@SpringBootApplication
@RefreshScope
@EnableFeignClients
public class BlogServiceApplication {

	@LoadBalanced
	@Bean
	public RestTemplate getTemplate()
	{
		return  new RestTemplate();
	}
	@Bean
	public WebClient.Builder getWebClient()
	{
		return WebClient.builder();
	}
	@Autowired
	private BeanFactory beanFactory;
	@Bean
	public Consumer<String> notificationEventSupplier()
	{
		return message -> {
			EmailSender.sendEmail(message);
		};
	}
//	@Bean
//	public RequestInterceptor requestInterceptor()
//	{
//		return new RequestInterceptor() {
//			@Override
//			public void apply(RequestTemplate requestTemplate) {
//
//			}
//		};
//	}
	@Bean
	public ExecutorService traceableExecutorService()
	{
		ExecutorService executorService = Executors.newCachedThreadPool();
		return new TraceableExecutorService(beanFactory,executorService);	}

	public static void main(String[] args) {
		SpringApplication.run(BlogServiceApplication.class, args);
	}

}
