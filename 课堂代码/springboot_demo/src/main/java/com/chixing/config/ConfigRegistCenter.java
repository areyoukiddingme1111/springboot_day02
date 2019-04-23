package com.chixing.config;

import com.chixing.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 配置中心
 * 拦截器、视图解析器的配置等 web
 */

/*@EnableWebMvc  //启用Spring MVC支持
@Configuration //允许在上下文中注册额外的bean或导入其他配置类*/
public class ConfigRegistCenter implements WebMvcConfigurer {


    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
                                                  /*  .excludePathPatterns("/login")
                                                    .excludePathPatterns("/regist");*/

    }


}
