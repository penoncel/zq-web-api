package com.mer.framework.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.mer.common.constant.Constant;
import com.mer.common.enums.ErrorStateEnum;
import com.mer.framework.web.domain.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 * http://127.0.0.1:7777/swagger-ui.html#/
 *
 * http://localhost:7777/doc.html
 * https://xiaoym.gitee.io/knife4j/documentation/
 */
@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {
    private final OpenApiExtensionResolver openApiExtensionResolver;

    public Swagger2Config(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean
    public Docket appApi() {
        String groupName="1.X版本";
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // 创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                .groupName(groupName)
                // 设置哪些接口暴露给Swagger展示
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mer.project.controller.app"))
                //指定扫描有Api注解的包
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .extensions(openApiExtensionResolver.buildSettingExtensions())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages()).ignoredParameterTypes(Result.class);
        return docket;
    }


    @Bean
    public Docket weChat() {
        String groupName="2.X版本";
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // 创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                .groupName(groupName)
                // 设置哪些接口暴露给Swagger展示
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mer.project.controller.weChat"))
                //指定扫描有Api注解的包
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .extensions(openApiExtensionResolver.buildSettingExtensions());
        return docket;
    }






    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger-bootstrap-ui-demo RESTful APIs")
                .description("XXXX-App接口文档，")
                .termsOfServiceUrl("www.baidu.com")
                .contact("赵旗")
                .version("1.0")
                .license("赵旗")
                .licenseUrl("https://github.com/penoncel/boot-shiro-jwt")
                .build();
    }

    /**
     * 自定义响应信息
     */
    private List<ResponseMessage> responseMessages() {
        List<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>() {{
            ErrorStateEnum success = ErrorStateEnum.SUCCESS;
            ErrorStateEnum invalid = ErrorStateEnum.TOKEN_ISNULL;
            ErrorStateEnum requestlimit = ErrorStateEnum.REQUEST_LIMIT;
            add(new ResponseMessageBuilder().code(success.getCode()).message(success.getMsg()).build());
            add(new ResponseMessageBuilder().code(invalid.getCode()).message(invalid.getMsg()).build());
            add(new ResponseMessageBuilder().code(requestlimit.getCode()).message(requestlimit.getMsg()).build());
        }};
        return responseMessages;
    }


    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey(Constant.TOKEN_HEADER_NAME, Constant.TOKEN_HEADER_NAME, "header"));
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }
    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(Constant.TOKEN_HEADER_NAME, authorizationScopes));
        return securityReferences;
    }
}
