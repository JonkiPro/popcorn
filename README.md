# REST-Web-Services
#### 1. Database: MySQL

#### 2. To use the "Remember Me" feature when logging in, you must perform a sql query in the database
```sql
create table persistent_logins (
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
);
```

#### 3. To properly launch an application with added Lombok annotations you must
- add Lombok Plugin
- enable annotation processing

## Tech Stack
*  Java
*  Spring
*  Spring MVC
*  Spring Boot
*  Spring Data
*  Spring Security
*  Hibernate / JPA / ORM
*  MySQL / SQL
*  Lombok
*  JSONDoc
*  Mail
*  WebJars
*  Thymeleaf
*  HTML
*  CSS
*  JavaScript
*  jQuery
*  AJAX
*  AngularJS
*  IntelliJ
*  Gradle
