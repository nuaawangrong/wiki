//package com.wangrong.wiki.config;
//
//import com.wangrong.wiki.interceptor.LoginInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Resource
//    LoginInterceptor loginInterceptor;
//
////    @Resource
////    ActionInterceptor actionInterceptor;
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**").excludePathPatterns("/login");
////                .excludePathPatterns(
////                        "/test/**",
////                        "/redis/**",
////                        "/user/login",
////                        "/category/all",
////                        "/ebook/list",
////                        "/doc/all/**",
////                        "/doc/vote/**",
////                        "/doc/find-content/**",
////                        "/ebook-snapshot/**"
////                );
//
////        registry.addInterceptor(actionInterceptor)
////                .addPathPatterns(
////                        "/*/save",
////                        "/*/delete/**",
////                        "/*/reset-password");
//    }
//}