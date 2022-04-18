package com.example.myproj.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket restApi()

    {
//     return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("Public-API")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.myproj"))
//             .paths(PathSelectors.any())
//                .build()
//             .apiInfo(apiInfo())
//             .securitySchemes(Arrays.asList(apiKey()));

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/v1/.*"))
                .build().groupName("API")
                .globalOperationParameters(newArrayList(
                        new ParameterBuilder().name(HttpHeaders.AUTHORIZATION).description("Authorization token").required(true)
                                .modelRef(new ModelRef("string")).parameterType("header").required(true).build()))
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder().title("REST API DOCUMENT")
                .description("work in progress")
                .termsOfServiceUrl("localhost")
                .version("1.0")
                .build();
    }

}
