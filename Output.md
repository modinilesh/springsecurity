# Output

````
.   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v4.1.2)

2026-10-24T02:43:42.003+05:30  INFO 32738 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Starting SpringsecurityApplication using Java 21.0.12.1 with PID 32738 (/Users/modin/Downloads/springsecurity/target/classes started by modin in /Users/modin/Downloads/springsecurity)
2026-10-24T02:43:42.005+05:30  INFO 32738 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : No active profile set, falling back to 1 default profile: "default"
2026-10-24T02:43:42.376+05:30  INFO 32738 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2026-10-24T02:43:42.383+05:30  INFO 32738 --- [springsecurity] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-10-24T02:43:42.383+05:30  INFO 32738 --- [springsecurity] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.27]
2026-10-24T02:43:42.402+05:30  INFO 32738 --- [springsecurity] [           main] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 370 ms
2026-10-24T02:43:42.559+05:30  INFO 32738 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-10-24T02:43:42.561+05:30  INFO 32738 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Started SpringsecurityApplication in 0.762 seconds (process running for 0.952)
2026-10-24T02:43:52.384+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2026-10-24T02:43:52.385+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2026-10-24T02:43:52.386+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-10-24T02:43:52.389+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] c.l.s.filters.MyCustomFilter             : Request with URL: http://localhost:8080/ came at filter at :Sat Oct 24 02:43:52 IST 2026
2026-10-24T02:43:52.396+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] c.l.s.interceptors.MyCustomInterceptor   : Request with URL: http://localhost:8080/ came to interceptor (preHandle) layer at Sat Oct 24 02:43:52 IST 2026
2026-10-24T02:43:52.396+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] c.l.s.interceptors.MyCustomInterceptor   : Logging response status just before controller method invocation, status: 200
2026-10-24T02:43:52.410+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] c.l.s.interceptors.MyCustomInterceptor   : Request with URL: http://localhost:8080/ came to interceptor (postHandle) layer at Sat Oct 24 02:43:52 IST 2026
2026-10-24T02:43:52.410+05:30  INFO 32738 --- [springsecurity] [nio-8080-exec-1] c.l.s.interceptors.MyCustomInterceptor   : Request with URL: http://localhost:8080/ came to interceptor (afterCompletion) layer at Sat Oct 24 02:43:52 IST 2026

````