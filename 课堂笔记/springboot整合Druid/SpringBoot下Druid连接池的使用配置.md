

## SpringBoot下Druid连接池的使用配置

### Druid介绍

Druid是一个JDBC组件，druid 是阿里开源在 github 上面的数据库连接池,它包括三部分：  
 \*   DruidDriver 代理Driver，能够提供基于Filter－Chain模式的插件体系。 
 \*   DruidDataSource 高效可管理的数据库连接池。 
 \*   SQLParser 专门解析 sql 语句

Druid 有什么优点

- 1. 可以监控数据库访问性能，Druid内置提供了一个功能强大的StatFilter插件，能够详细统计SQL的执行性能，这对于线上分析数据库访问性能有帮助。 
  2. 替换DBCP和C3P0。Druid提供了一个高效、功能强大、可扩展性好的数据库连接池。 
  3.  数据库密码加密。直接把数据库密码写在配置文件中，这是不好的行为，容易导致安全问题。DruidDruiver和DruidDataSource都支持PasswordCallback。 
  4. SQL执行日志，Druid提供了不同的LogFilter，能够支持Common-Logging、Log4j和JdkLog，你可以按需要选择相应的LogFilter，监控你应用的数据库访问情况。
  5. 扩展JDBC，如果你要对JDBC层有编程的需求，可以通过Druid提供的Filter-Chain机制，很方便编写JDBC层的扩展插件。



 **Springboot配置Druid**

（1）pom.xml

```xml
<!--
   引入druid数据源
   https://mvnrepository.com/artifact/com.alibaba/druid
-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.10</version>
</dependency>

```

（2）application.yml

```yml

# 数据源配置，整合druid 
spring:
  datasource:
    druid:
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql://localhost:3306/tripdb?serverTimezone=UTC&characterencoding=utf-8
      username: root
      password: root

      # 初始化连接数量
      initial-size: 5
      # 最大连接池数量
      max-active: 30
      # 最小连接池数量
      min-idle: 5
      # 获取连接时最大等待时间，单位毫秒
      max-wait: 60000
      # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
      time-between-eviction-runs-millis: 60000
      # 连接保持空闲而不被驱逐的最小时间
      min-evictable-idle-time-millis: 300000
      # 用来检测连接是否有效的sql，要求是一个查询语句
      validation-query: SELECT 1 FROM DUAL
      # 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
      test-while-idle: true
      # 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
      test-on-borrow: false
      # 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
      test-on-return: false
      # 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
      pool-prepared-statements: true
      # 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。
      max-pool-prepared-statement-per-connection-size: 50
      # 配置监控统计拦截的filters，去掉后监控界面sql无法统计
      filters: stat,wall,log4j2
      # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
      # 合并多个DruidDataSource的监控数据
      useGlobalDataSourceStat: true
      stat-view-servlet:
        url-pattern: /druid/*
        enabled: true # 是否启用StatFilter默认值false
        filter:
          stat:
            log-slow-sql: true
            slow-sql-millis: 1000
            merge-sql: false
          wall:
            config:
              multi-statement-allow: true
               

```

（3）测试

```java
@Autowired
DataSource dataSource;

@Test
public void contextLoads() throws SQLException {
   System.out.println("dataSource = " + dataSource.getClass());
   Connection connection = dataSource.getConnection();
   System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>connection = " + connection);
   connection.close();
}
```

### 启动SpringBoot+Druid 

#### 方式一：基于Annotation

1. 配置一个管理后台的Servlet， 启动druid 的监控web模块

```java
  
   @SuppressWarnings("serial")
   @WebServlet(urlPatterns = "/druid/*",
           initParams={
                   @WebInitParam(name="allow",value=""),// IP白名单 (没有配置或者为空，则允许所有访问)
                   @WebInitParam(name="deny",value="192.168.16.111"),// IP黑名单 (存在共同时，deny优先于allow)
                   @WebInitParam(name="loginUsername",value="admin"),// 用户名
                   @WebInitParam(name="loginPassword",value="abc123"),// 密码
                   @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能
           })
   public class DruidStatViewServlet extends StatViewServlet {
   
   }
   
```

2. 配置一个web监控的filter

```java
@WebFilter(filterName="druidWebStatFilter",urlPatterns="/*",
        initParams={
                @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
        })
public class DruidStatFilter extends WebStatFilter {

}
```

3. 配置数据源。这里相关的参数会自动赋值到datasource里。

   ```java
   @Configuration
   //标识该类被纳入spring容器中实例化并管理
    @ServletComponentScan
   // 用于扫描所有的Servlet、filter、listener
   public class DruidConfig {
       @Bean
       @ConfigurationProperties(prefix = "spring.datasource")
       @Primary //在同样的DataSource中，首先使用被标注的DataSource
       //加载时读取指定的配置信息,前缀为spring.datasource.druid
       public DataSource druidDataSource() {
           return new DruidDataSource();
       }
   }
   ```

4. 启动类 Application.java

   ```java
   @ServletComponentScan  // 扫描Servlet，用于扫描与创建Druid后台管理的Servlet，
   @SpringBootApplication // SpringBoot项目启动入口
   public class SpringboorDatasourceApplication {
   
      public static void main(String[] args) {
         SpringApplication.run(SpringboorDatasourceApplication.class, args);
      }
   }
   ```



### 方式二：基于编程式

```java
@Configuration
public class DruidConfiguration {
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
     private String username;
     @Value("${spring.datasource.password}")
    private String password;
     @Value("${spring.datasource.driverClassName}")
     private String driverClassName;
     @Value("${spring.datasource.initialSize}")
     private int initialSize;
     @Value("${spring.datasource.minIdle}")
     private int minIdle;
     @Value("${spring.datasource.maxActive}")
     private int maxActive;
     @Value("${spring.datasource.maxWait}")
     private int maxWait;
     @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
     private int timeBetweenEvictionRunsMillis;
     @Value("${spring.datasource.minEvictableIdleTimeMillis}")
     private int minEvictableIdleTimeMillis;
     @Value("${spring.datasource.validationQuery}")
     private String validationQuery;
     @Value("${spring.datasource.testWhileIdle}")
     private boolean testWhileIdle;
     @Value("${spring.datasource.testOnBorrow}")
     private boolean testOnBorrow;
     @Value("${spring.datasource.testOnReturn}")
     private boolean testOnReturn;
     @Value("${spring.datasource.poolPreparedStatements}")
     private boolean poolPreparedStatements;
     @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
     private int maxPoolPreparedStatementPerConnectionSize;
     @Value("${spring.datasource.filters}")
     private String filters;
     @Value("{spring.datasource.connectionProperties}")
     private String connectionProperties;

 @Bean //声明其为Bean实例
 @Primary //在同样的DataSource中，首先使用被标注的DataSource
 public DataSource dataSource(){
     DruidDataSource datasource = new DruidDataSource();
     datasource.setUrl(this.dbUrl);
     datasource.setUsername(username);
     datasource.setPassword(password);
     datasource.setDriverClassName(driverClassName);

     //configuration
     datasource.setInitialSize(initialSize);
     datasource.setMinIdle(minIdle);
     datasource.setMaxActive(maxActive);
     datasource.setMaxWait(maxWait);
     datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
     datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
     datasource.setValidationQuery(validationQuery);
     datasource.setTestWhileIdle(testWhileIdle);
     datasource.setTestOnBorrow(testOnBorrow);
     datasource.setTestOnReturn(testOnReturn);
     datasource.setPoolPreparedStatements(poolPreparedStatements);
     datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
     try {
        datasource.setFilters(filters);
     } catch (SQLException e) {
        System.err.println("druid configuration initialization filter: "+ e);
     }
        datasource.setConnectionProperties(connectionProperties);
     return datasource;
     }

     @Bean
     public ServletRegistrationBean statViewServle(){
         ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
         // IP白名单
         servletRegistrationBean.addInitParameter("allow","");
         // IP黑名单(共同存在时，deny优先于allow)
         servletRegistrationBean.addInitParameter("deny","192.168.1.100");
         //控制台管理用户
         servletRegistrationBean.addInitParameter("loginUsername","admin");
         servletRegistrationBean.addInitParameter("loginPassword","abc123");
         //是否能够重置数据
         servletRegistrationBean.addInitParameter("resetEnable","false");
         return servletRegistrationBean;
     }

     @Bean
     public FilterRegistrationBean statFilter() {
         FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
         //添加过滤规则
         filterRegistrationBean.addUrlPatterns("/*");
         //忽略过滤的格式
         filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
         return filterRegistrationBean;
     }
 }
```

创建启动类 

```java
@ServletComponentScan  // 扫描Servlet，用于扫描与创建Druid后台管理的Servlet，
@SpringBootApplication // SpringBoot项目启动入口
public class SpringboorDatasourceApplication {

   public static void main(String[] args) {
      SpringApplication.run(SpringboorDatasourceApplication.class, args);
   }
}
```



现在可以访问Druid的监控平台

http://localhost:8080/druid/login.html

Druid 介绍

https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98





注解说明：

| 序号 | 注解                                                    | 说明                                                         |
| ---- | ------------------------------------------------------- | ------------------------------------------------------------ |
| 1    | @Configuration                                          | 标识该类被纳入spring容器中实例化并管理，等同于spring的XML配置文件；使用Java代码可以检查类型安全 |
| 2    | @Bean                                                   | 可理解为用spring的时候xml里面的标签                          |
| 3    | @ServletComponentScan(basePackages = { “com.chixing” }) | 扫描工程中的Servlet、Filter、Listener（带注解的）            |
| 4    | @RunWith(SpringJUnit4ClassRunner.class)                 | SpringJUnit支持，由此引入Spring-Test框架支持！               |
| 5    | @SpringApplicationConfiguration(classes = App.class)    | 指定我们SpringBoot工程的Application启动类（App是项目的启动类） |
| 6    | @WebAppConfiguration                                    | 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。 |
| 7    | @Value(“${application.username:QiuRiMangCao}”           | 使用@value注解，从application.properties配置文件读取值，没读取到就用默认值 |
| 8    | @Primary                                                | 注解的实例优先于其他实例被注入                               |
| 9    | @ConfigurationProperties                                | 可以把同类的配置信息自动封装成实体类                         |
| 10   | @WebServlet                                             | 标记为servlet，以便启动器扫描。                              |
| 11   | @WebFilter                                              | 标记为Filter，以便启动器扫描。                               |

