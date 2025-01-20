# Quarkus 集成 MyBatis

+ quarkus-mybatis

  https://github.com/quarkiverse/quarkus-mybatis/blob/master/mybatis/docs/modules/ROOT/pages/index.adoc



## 遇到的问题

### Quarkus 其他模块定义的 Bean 无法直接注入的问题

经测试，好像其他模块定义的 Bean 好像无法直接注入到当前模块。其他模块定义的 @Mapper 接口也无法直接注入到当前模块，推测和Spring Boot 会为 Mybatis @Mapper 接口通过动态代理生成 Bean 实例一样，Quarkus 也会为 @Mapper 接口生成的 Bean。

```java
// 无法直接注入其他模块中定义的 @Mapper Bean  
@Inject
IAwardDao awardDao;
```

具体原因，需要看 CDI 内部实现原理，TODO 。
