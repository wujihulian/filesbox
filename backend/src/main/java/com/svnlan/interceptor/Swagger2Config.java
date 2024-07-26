package com.svnlan.interceptor;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
//        List<Parameter> parameters = new ArrayList<>();
//        parameters.add(new ParameterBuilder()
//                .name("token")
//                .description("token")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build()
//        );
//        parameters.add(new ParameterBuilder()
//                .name("Cookie")
//                .description("Cookie")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build()
//        );
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.regex("/api/jwt/.*")) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
//                .globalOperationParameters(parameters);
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> list =  new ArrayList();
        list.add(new ApiKey("token", "token", "header"));
        return list;
    }


    private List<SecurityContext> securityContexts() {
        List<SecurityContext> list =  new ArrayList();
        list.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                //         .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
        return list;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> list =  new ArrayList();
        list.add(new SecurityReference("token", authorizationScopes));
        return list;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Jwt服务") //设置文档的标题
                .version("2.0.0") // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }
    /**
     * 配置swagger2的静态资源路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}