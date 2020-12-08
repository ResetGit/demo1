package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer  {
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login.html").setViewName("/login.html");
//
//    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
//                .allowedHeaders("*")
//                //cookie设置
//                .allowCredentials(true)
//                .maxAge(3600);
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginHandlerInterceptor handlerInterceptor = new LoginHandlerInterceptor();
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("user/GetSession")
                .addPathPatterns("index.html")
                .addPathPatterns("/index.html")
                .addPathPatterns("*/index.html")
                .addPathPatterns("/index.html/")
                .addPathPatterns("/")
                .excludePathPatterns("*.js")
                .excludePathPatterns("*.gif")
                .excludePathPatterns("*.css")
                .excludePathPatterns("*.png")
                .excludePathPatterns("*.ttf")
                .excludePathPatterns("*.woff")
                .excludePathPatterns("*.jpg");

    }



}
