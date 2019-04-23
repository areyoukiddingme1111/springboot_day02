 在项目的开发中，日志是必不可少的一个记录事件的组件，所以也会相应的在项目中实现和构建我们所需要的日志框架。

市面上常见的日志框架有很多，比如：JCL、SLF4J、Jboss-logging、jUL、log4j、log4j2、logback等等，我们该如何选择呢？

通常情况下，日志是由一个抽象层+实现层的组合来搭建的。

| **日志-抽象层**           | **日志-实现层**             |
| ------------------------- | --------------------------- |
| JCL、SLF4J、jboss-logging | jul、log4j、log4j2、logback |

而SpringBoot的选择了SLF4J+Logback的组合，这个组合是当下比较合适的一组

日志级别从低到高分为：

> `ALL< TRACE` < `DEBUG` < `INFO` < `WARN` < `ERROR` < `FATAL`<OFF

```
all : 最低等级，用于打开所有日志记录
trace: 一个很低的日志级别，一般使用不到
debug: 指出细粒度信息事件对调试应用程序较有帮助，主要应用再开发过程中打印出来的运行信息
info:消息再粗粒度级别上突出强调程序的运行过程。这个可以应用在生产环境中输出的程序运行信息，但是不要滥用，避免打印过多的无用的日志。
warn: 表明出现潜在的错误，警告。
error: 指出程序发生错误，但任然不影响系统继续运行。打印输出错误和异常信息。
fatal: 指出每个严重的错误事件导致应用程序的退出，该级别比较高，重大错误并可以停止程序执行。
off: 最高等级，关闭所有的日志记录。
```







1. #### 首先，引入AOP的依赖包

```xml
<dependency>  
   	<groupId>org.springframework.boot</groupId>  
   	<artifactId>spring-boot-starter-aop</artifactId>  
</dependency> 

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
</dependency>
```

2. #### application.yml

```yaml
logging:
  file: d://logger/mylogger.log # 日志记录的文件位置
  level:
    com.example: debug
```

3. #### resources目录下加入 log4j/log4j2.xml

   









  日志测试

```java
@SpringBootApplication
public class SpringbootFirstApplication {

   static Logger logger = LoggerFactory.getLogger(Logger.class);
   public static void main(String[] args) {
      logger.trace("trace.....");
      logger.debug("debug.....");
      logger.info("info.....");
      logger.warn("warn.....");
      logger.error("error.....");

      SpringApplication.run(SpringbootFirstApplication.class, args);
   }
}
```

运行结果：

```
[main] DEBUG org.slf4j.Logger - debug.....
 [main] INFO org.slf4j.Logger - info.....
[main] WARN org.slf4j.Logger - warn.....
 [main] ERROR org.slf4j.Logger - error.....
```

可以看到，日志只输出了degug、info、warn、error 。



AOP即面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。如果几个或更多个逻辑过程中，有重复的操作行为，AOP就可以提取出来，运用动态代理，实现程序功能的统一维护，这样就非常方便了，在实现主业务过程中无需为一些零碎的但必不可少的旁支功能打扰，而是后期横切进去.

 

**新建一个HttpAspect类，放在aspect包下：**

```java
@Aspect
@Component
public class HttpAspect {	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(public * org.chixing.controller.ItemsController1.*(..))")   
	public void myLog() {    }		
	
	@Before("myLog()")	
	public void reqMessage() {		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();        
        HttpServletRequest request = attributes.getRequest();       
		logger.info("*********打印请求信息开始**********");		
		logger.info("URL : " + request.getRequestURL().toString());   
		logger.info("HTTP_METHOD : " + request.getMethod());     
		logger.info("*********打印请求信息结束**********");	
	}	

	@AfterReturning(returning="object",pointcut="myLog()")	
	public void resMessage(Object object) {		
		logger.info("*********打印结果信息开始**********");	
		logger.info("RESULT:"+ object);		
		logger.info("*********打印结果信息结束**********");
	}
}
```

就这样，其余代码都不变，我们运行之后，输入http://localhost:8080/user，控制台显示打印信息即可。

@Aspect    作用是把当前类标识为一个切面供容器读取。

@Before     标识一个前置增强方法，相当于BeforeAdvice的功能，从切入点开始处切入内容。

@AfterReturning      后置增强，相当于AfterReturningAdvice，方法正常退出时执行。

@Pointcut      定义一个切入点，可以是一个规则表达式，也可以是一个注解等





 **配置日志生成的路径及日志名称**

在项目的运行中，我们不可能一直看着控制台，而且日质量会很大，转瞬即逝的~

![](D:\chixing_course\09 Frameworks\SpringBoot\springboot_02\1.JPG)

那么，我们需要指定我们需要的日志名称以及日志生成的路径，用到两个配置，都是在application.properties/yml中写，如下：（都不设置的话，不生成日志）

```
# 按照默认的名称spring.log，生成到指定路径及日志。
#logging.path=output/logs
# 不指定的情况下默认生成在项目根目录，按照配置生成所需的日志名称
logging.file=D:\logger\myLogger.log
```

 



### 配置自定义log信息

如果想用自己的log配置，不用系统默认的，那么只需要按照官方要求，将该配置文件放在所需类的目录下即可，也可以在resource中配置全局的

![](D:\chixing_course\09 Frameworks\SpringBoot\springboot_02\2.JPG)



然而官方推荐我们在这些命名中，使用带有spring的扩展名，它会被SpringBoot框架识别（不写的单会被日志框架识别），并且可以使用其相应的功能，比如根据环境来使用某段配置：

![1537938235242](C:\Users\qianghj\AppData\Local\Temp\1537938235242.png)