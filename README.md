# easyms-sdk
Mutli function starter for Java microservice projets based on spring-boot and docker

# How to compile
+ Use at least Java 11 and maven 3.5.2`

```
> mvn clean install
```

# How to launch sample app
+ With main class : com.easyms.sampleapp.EasyMsSampleApplication
```
  -Dspring.config.location=$MODULE_DIR$/target/test-classes/
  -Dspring.config.name=easyms-sample-ms
  -Dlogging.name=$PATH_TO_TARGET$/target/test-classes/logback-console.xml
  -Xms128m
  -Xmx256m

 PATH_TO_TARGET : is path to your build dir. Under Intellij use can use $MODULE_DIR$ variable

``


`