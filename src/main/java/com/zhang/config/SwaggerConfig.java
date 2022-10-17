package com.zhang.config;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2 // 开启swagger2
public class SwaggerConfig {
    // 开关配置，此处关联配置文件中的是否启用swagger2的开关字段swagger.enable
    @Value("${swagger.enable}")
    private boolean enable; // enable的值就等于swagger.enable

    // swagger文档的配置
    @Bean
    public Docket createDocket() {
        // 全局的swagger入口。作用是使swagger发起head请求
        List<Parameter> lists = new ArrayList<>();
        ParameterBuilder accessToken = new ParameterBuilder(); // 准许进入的Token
        ParameterBuilder refreshToken = new ParameterBuilder();// 刷新的Token
        // 设置accessToken的规则
        // 参数名为authorization，描述内容"程序员自己测试时动态传入Token的入口。"，参数的类型是String（string注意是小写），请求的方式是header请求，是否必须为false
        accessToken.name("authorization").description("程序员自己测试时动态传输accessToken的入口。")
                .modelRef(new ModelRef("string")).parameterType("header").required(false);
        // 设置refreshToken的规则
        refreshToken.name("refreshToken").description("程序员自己测试时动态传输refreshToken的入口。")
                .modelRef(new ModelRef("string")).parameterType("header").required(false);

        // 将设置好的accessToken规则和refreshToken规则传入集合
        lists.add(accessToken.build());
        lists.add(refreshToken.build());

        return new Docket(DocumentationType.SWAGGER_2) // 类型
                .apiInfo(apiInfo()) // 描述 // 调用我们自己的方法
                .select() // 扫描
                .apis(RequestHandlerSelectors.basePackage("com.zhang.controller")) // 指定扫描的包
                .paths(PathSelectors.any()) // 扫描范围是any，即扫描所有
                .build()// 构建操作
                .globalOperationParameters(lists) // 将上面设置的两种token规则传入
                .enable(enable); // 根据配置文件中的值来判断是否启用swagger
    }

    // 配置api文档的描述
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("shiro+jwt+redis实战")
                .description("后端Api")
                .termsOfServiceUrl("") // 扫描的Url
                .version("1.0")
                .build();// 执行的操作：构建
    }

}

