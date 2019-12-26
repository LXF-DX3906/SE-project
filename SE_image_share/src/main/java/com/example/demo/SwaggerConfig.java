package com.example.demo;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: SE_imsge_share
 * @description: swagger配置类
 * @author: Xuefei Lv
 * @create: 2019/12/25
 **/

// 启动时加载类
@Configuration
// 启用Swagger API文档
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag("图片控制类", "负责展示，存储图片信息"),
                        new Tag( "用户图片交互类","负责用户与图片间的交互，包括点赞，收藏等"),
                        new Tag("相册控制类", "负责控制相册信息"),
                        new Tag("登录控制类", "负责用户的登录及注册"),
                        new Tag("用户信息控制类", "负责用户信息的展示及修改"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("image-share")
                .description("image-share API 1.0 Swagger文档")
                .termsOfServiceUrl("http://swagger.io/")
                .version("1.0")
                .build();
    }

}
