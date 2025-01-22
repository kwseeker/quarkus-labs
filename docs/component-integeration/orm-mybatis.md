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

### quarkus-mybatis 指定配置和Mapper文件路径时不支持 `classpath: `格式

网上没有找到这个问题的解决方法，只能看源码了，加载文件的代码在 MyBatisRecorder#buildFromMapperLocations 中实现。

**其实 `classpath: `和 `classpath*:` 并不是 JDK 中标准的写法，是 Spring 中定义的写法，会对这两个开头的路径进行特殊处理**。

```java
private void buildFromMapperLocations(Configuration configuration, MyBatisRuntimeConfig myBatisRuntimeConfig,
                                      String dataSourceName) {
    myBatisRuntimeConfig.mapperLocations.ifPresent(mapperLocations -> {
        for (String mapperLocation : mapperLocations) {
            try {
                // 这两个if, 用于将mapperLocation前后的“/”去掉，即获取相对路径
                if (mapperLocation.endsWith("/")) {
                    mapperLocation = mapperLocation.substring(0, mapperLocation.length() - 1);
                }
                if (mapperLocation.startsWith("/")) {
                    mapperLocation = mapperLocation.substring(1);
                }
                // 根据相对路径加载资源，相对路径是相对于 classpath (target/classes) 
                final URL resource = Thread.currentThread().getContextClassLoader()
					.getResource(mapperLocation);
                if (resource != null) {
                    final String path = resource.getFile();
                    if (path != null && path.contains("jar!")) {
                        File resourceFile = Paths.get(new URL(path.substring(0, path.indexOf("!"))).toURI()).toFile();
                        try (JarFile jarFile = new JarFile(resourceFile)) {
                            Enumeration<JarEntry> entries = jarFile.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry entry = entries.nextElement();
                                String resourceName = entry.getName();
                                if (!entry.isDirectory() && resourceName.startsWith(mapperLocation)
                                    && !resourceName.endsWith(".class") && resourceName.endsWith(".xml")) {
                                    buildXmlMapper(jarFile.getInputStream(entry), jarFile.getInputStream(entry),
                                                   entry.toString(), configuration, dataSourceName);
                                }
                            }
                        }
                    } else if (path != null) {
                        // 获取相对路径下所有文件
                        final File[] files = new File(path).listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.getName().endsWith(".xml")) { //使用 .xml 文件构建 XmlMapper
                                    buildXmlMapper(new FileInputStream(file), new FileInputStream(file),
                                                   file.toString(),
                                                   configuration, dataSourceName);
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException | IOException | URISyntaxException e) {
                LOG.warnf("Not found mapper location :%s.", mapperLocation);
            } catch (ClassNotFoundException e) {
                // 抛出异常后就会结束当前的 mapperLocation 的继续解析，
				// 即便还有 xml 文件没有解析且存在 Mapper 类，也不会加载
                LOG.warnf("Not found mapper class :%s.", e.getMessage());
            }
        }
    });
}

private void buildXmlMapper(InputStream filterStream, InputStream resourceStream, String resource,
                            Configuration configuration,
                            String dataSourceName)
    throws ClassNotFoundException {
    final XPathParser xPathParser = new XPathParser(filterStream,
		true, configuration.getVariables(), new XMLMapperEntityResolver());
    // !!! 这里会先找 namespace 对应的 Mapper 类，进行加载，如果类不存在会抛出异常，由 buildFromMapperLocations 中的代码可知抛出异常后就会结束当前的 mapperLocation 的继续解析，即便后面还有 xml 没有解析且存在 Mapper 类，也不会加载。
    String nameSpace = xPathParser.evalNode("/mapper").getStringAttribute("namespace");
    final Class<?> mapperClass = Resources.classForName(nameSpace);
    final MapperDataSource annotation = mapperClass.getAnnotation(MapperDataSource.class);
    if ((annotation != null && annotation.value().equals(dataSourceName))
        || (annotation == null && dataSourceName.equals("<default>"))) {
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resourceStream,
			configuration, resource, configuration.getSqlFragments());
        xmlMapperBuilder.parse();
    }
}
```

