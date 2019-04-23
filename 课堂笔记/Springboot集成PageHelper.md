Springboot集成PageHelper

1. maven的依赖

```xml
<!-- ========================= pagehelper ================-->
<dependency>
   <groupId>com.github.pagehelper</groupId>
   <artifactId>pagehelper-spring-boot-starter</artifactId>
   <version>1.2.3</version>
</dependency>
```

2. application.yml

   ```yaml
   pagehelper:
     helper-dialect: mysql
     reasonable: true
     support-methods-arguments: true
     params: count=countSql
   ```

   