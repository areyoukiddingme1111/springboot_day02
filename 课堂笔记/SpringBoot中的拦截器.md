# SpringBoot中的拦截器

1. 定义拦截器类

   ```java
   public class MyInterceptor extends HandlerInterceptorAdapter {
       public MyInterceptor(){
           System.out.println("MyInterceptor contructor...........");
       }
   
       //在执行目标方法之前
       @Override
       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
               System.out.println("MyInterceptor preHandle");
               return true;
       }
   
       //在执行目标方法之后，在目标方法return 返回响应视图之前
       @Override
       public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
           System.out.println("MyInterceptor postHandle");
       }
   
       //在目标方法return 返回响应视图之后
       @Override
       public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
           System.out.println("MyInterceptor afterCompletion");
       }
   }
   ```

2. config/ConfigRegistCenter

   ```java
   /**
    * 配置中心
    * 拦截器、视图解析器的配置等 web
    */
   
   @EnableWebMvc  //启用Spring MVC支持
   @Configuration //允许在上下文中注册额外的bean或导入其他配置类
   public class ConfigRegistCenter implements WebMvcConfigurer {
   
   
       //注册拦截器
       @Override
       public void addInterceptors(InterceptorRegistry registry) {
           registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
                                                     /*  .excludePathPatterns("/login")
                                                       .excludePathPatterns("/regist");*/
   
       }
   }
   ```