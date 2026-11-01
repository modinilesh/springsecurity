# Spring Security — notes

## Authentication vs Authorization

**Authentication** is proving who the user is. We take credentials (username/password, token, etc.) and verify them against what we already know. If they match, the user is a known principal in the application. If they do not, the request stops here.

**Authorization** comes after that. We already know who the caller is; now we decide whether that identity is allowed to hit this URL, method, or feature. Authentication answers “who are you?” Authorization answers “are you allowed to do this?”

A user can be authenticated and still get 403. That is expected. Login succeeded; permission did not.

---

## Authentication workflow (request path)


![Spring Security Authentication Workflow](docs/Spring%20Security%20Authentication%20Workflow.png)


---

## Ways to wire this up

### Step 0 — add the dependency

Add `spring-boot-starter-security` dependency in pom. Once this is added and application is restarted, you see similar logs as below:
```
 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.1.2)

2026-11-01T14:15:20.067+05:30  INFO 16792 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Starting SpringsecurityApplication using Java 21.0.12.1 with PID 16792 (/Users/modin/Downloads/springsecurity/target/classes started by modin in /Users/modin/Downloads/springsecurity)
2026-11-01T14:15:20.069+05:30  INFO 16792 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : No active profile set, falling back to 1 default profile: "default"
2026-11-01T14:15:20.489+05:30  INFO 16792 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2026-11-01T14:15:20.496+05:30  INFO 16792 --- [springsecurity] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-11-01T14:15:20.497+05:30  INFO 16792 --- [springsecurity] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.27]
2026-11-01T14:15:20.519+05:30  INFO 16792 --- [springsecurity] [           main] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 418 ms
2026-11-01T14:15:20.677+05:30  WARN 16792 --- [springsecurity] [           main] .s.a.UserDetailsServiceAutoConfiguration : 

**Using generated security password: a0e46a71-a39a-4eef-90b0-b71c050c63be**

This generated password is for development use only. Your security configuration must be updated before running your application in production.

2026-11-01T14:15:20.696+05:30  INFO 16792 --- [springsecurity] [           main] r$InitializeUserDetailsManagerConfigurer : Global AuthenticationManager configured with UserDetailsService bean with name inMemoryUserDetailsManager
2026-11-01T14:15:20.773+05:30  INFO 16792 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-11-01T14:15:20.777+05:30  INFO 16792 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Started SpringsecurityApplication in 0.926 seconds (process running for 1.162)

```
This security password will be needed to access the endpoint with the username as "user". This is the default behaviour of Spring Security.

### 1. Users in `application.properties` / How to use custom username and passwords ?

Use this when the user set is tiny and basically static — a couple of admin accounts for a company portfolio site, an internal demo, local bootstrapping. Spring Boot’s default user is this model:

```properties
spring.security.user.name=admin
spring.security.user.password=admin
```

Good for learning and for apps that will never grow a real user table. Weak for anything production: no hashing strategy you control, no roles per user beyond the default, no user management, credentials sitting in config.

### 2. Users in the database / How DB users can login ?

This is the production-grade login path. We implement `UserDetailsService` (or use JDBC/JPA user details), load the account from the DB, and verify with a `PasswordEncoder`. Roles and authorities live with the user record, so authorization can actually vary per account.

Same filter / `AuthenticationManager` / `DaoAuthenticationProvider` flow as above. Only `loadUserByUsername()` changes — it hits the database instead of properties or in-memory users.
