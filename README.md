# easyms-sdk
Mutli function starter for Java microservice projets based on spring-boot and docker

# Summary of set up
+ Install Git
+ Install IntelliJ
+ Install at least Java 11
+ Install at least maven 3.5.2

# Project organization : checkout always master branch
+ /home/Dev/workspace
    - /easyms-sdk
    ```
    > git clone https://github.com/easyms/easyms-sdk.git
    ```
    - /easyms-config-server
     ```
      > git clone https://github.com/easyms/easyms-config-server.git
     ```
    - /easyms-oauth2
     ```
      > git clone https://github.com/easyms/easyms-oauth2.git
     ```

# Intellij Configuration :
+ Create new Empty Project.
+ In Project Structure:
    - Set Project SDK to jdk 11 in Project tab. 
    - In Modules tab, click on + sign to import new modules, Select easyms-sdk and choose Import module from external model (Maven).
    - Do the same thing for easyms-config-server and easyms-oauth2.

# Build Project :
+ Current path: /home/Dev/workspace
```
> cd easyms-sdk/
> mvn clean install
> cd ../easyms-config-server/
> mvn clean install
> cd ../easyms-oauth2/
> mvn clean install
```

# How to launch Secured Sample App
+ Click on Add New Configuration and choose Application

## Easyms-Config-Server
+ With main class : com.easyms.config.EasymsConfigServerApplication
+ With module : easyms-config-server
+ With JRE : 11

## Easyms-Oauth2
+ With main class : com.easyms.security.OAuth2Application
+ With VM Options :
```
-DSpring.profiles.active=local,h2,dev
```
+ With module : easyms-oauth2
+ With JRE : 11
## Easyms-Secured-Sample-Ms
+ With main class : com.easyms.sampleapp.EasyMsSecuredSampleApplication
+ With VM Options :
```
-DSpring.profiles.active=local,h2,dev
```
+ With module : easyms-secured-sample-ms 
+ With JRE : 11

# H2 Database Access:
+ easyms-oauth2 : http://localhost:2000/h2-console
```
    JDBC URL: jdbc:h2:mem:oauth2db;DB_CLOSE_DELAY=-1
    User Name: oauth2
    Password: secret
```
+ easyms-secured-sample-app : http://localhost:8091/h2-console
```
    JDBC URL: jdbc:h2:mem:sdkdb;DB_CLOSE_DELAY=-1
    User Name: sdk
    Password: secret
```
# Swagger Access:
+ easyms-oauth2 : http://localhost:2000/swagger-ui.html
+ easyms-secured-sample-app : http://localhost:8091/swagger-ui.html


