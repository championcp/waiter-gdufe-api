package com.waiterxiaoyy.backandroiddesign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author     ：WaiterXiaoYY
 * @description：配置Swagger
 */

@Configuration //配置类
@EnableSwagger2// 开启Swagger2的自动配置
public class SwaggerConfig {

    @Bean //配置docket以配置Swagger具体参数
    public Docket docket() {
//        ParameterBuilder ticketPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        ticketPar.name("token").description("token")
//                .modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build(); //header中的ticket参数非必填，传空也可以
//        pars.add(ticketPar.build());  //根据每个方法名也知道当前方法在设置什么参数

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())        // 将文档信息注入
                .groupName("waiterxiaoyy") // 分组名
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.waiterxiaoyy.backandroiddesign.controller")) // 扫描接口路径
                .build();
    }

    //配置文档信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("WaiterXiaoYY", "http://waiterxiaoyy.ltd/", "waiterxiaoyy@qq.com");
        return new ApiInfo(
                "接口文档", // 标题
                "[状态码：200-请求成功（返回数据） // 202-请求获取失败（捕获异常返回）]" , // 描述
                "v1.0", // 版本
                "", // 组织链接
                contact, // 联系人信息
                "", // 许可
                "", // 许可连接
                new ArrayList()// 扩展
        );
    }
}
