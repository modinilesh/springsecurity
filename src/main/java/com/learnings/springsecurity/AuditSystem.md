**Inbound Requests** : any requests which are coming to the server
**Outbound Requests** : any requests which we are making from the server to outside

# Why logging is needed ?
- To track the system at the api level, helpful in debugging
- To collect the useful data like how many request we served this year, what was the failure rates, avg response times etc
- To maintain the audits of external vendor calls to identify the correct costing and match with the billing

# What are the ways to achieve this ?
- **Inbound api audit** : these are in more control of the server e.g. failures can be handled better way
    - Best way is to implement at Filter level (refer the PR)
    - why: as filter is the entry and exit points for any request coming to the server
- **Outbound api audit** : as we can make the external api call using restTemplate, restClient, webClient, spring integration, and mechanism is different from each other !
  - This makes this problem as an interesting ! 
  - approach1 : If the system has the single mechanism used for outbound/vendor api calls then look for their supported Filters available 
    - For e.g. WebClient offers ExchangeFilterFunction which gets called just before and after of api call
  - approach2 : If we want to have a global supported system which supports all of these mechanisms, then we can simply invoke that bean and appropriate methods
    - [drawback]: dev needs to explicitly remember to call logging methods

# System at scale !
## Challenges : 
- if you handle the audit requests within the same server, it will affect the latency of the api, different services might be needing the write access to the audit db 
- what if multiple requests came at a time, it will directly create load on audit db 

## Suggested solution : use event driven calls
- Rather direct db commits, server should push request and response events to the message broker
- audit-service needs to created who will continuously listen to those topics and commits in db at it's own speed

------------------------------------------

Moving forward, 
Why ordering of filter is required ? 
- Suppose you created multiple filters as per the requirements, what do you think which will get execute first ? 
- I added two filters InboundApiFilter (audit logs) and TraceIdFilter (sets the traceId) in the same order
- Observe the below output carefully !
````
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.1.2)

2026-10-27T00:01:21.553+05:30  INFO 32325 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Starting SpringsecurityApplication using Java 21.0.12.1 with PID 32325 (/Users/modin/Downloads/springsecurity/target/classes started by modin in /Users/modin/Downloads/springsecurity)
2026-10-27T00:01:21.555+05:30  INFO 32325 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : No active profile set, falling back to 1 default profile: "default"
2026-10-27T00:01:21.932+05:30  INFO 32325 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2026-10-27T00:01:21.940+05:30  INFO 32325 --- [springsecurity] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-10-27T00:01:21.940+05:30  INFO 32325 --- [springsecurity] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.27]
2026-10-27T00:01:21.964+05:30  INFO 32325 --- [springsecurity] [           main] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 382 ms
2026-10-27T00:01:22.124+05:30  INFO 32325 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-10-27T00:01:22.127+05:30  INFO 32325 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Started SpringsecurityApplication in 0.779 seconds (process running for 0.984)
2026-10-27T00:01:34.919+05:30  INFO 32325 --- [springsecurity] [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2026-10-27T00:01:34.919+05:30  INFO 32325 --- [springsecurity] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2026-10-27T00:01:34.920+05:30  INFO 32325 --- [springsecurity] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-10-27T00:01:34.925+05:30  INFO 32325 --- [springsecurity] [nio-8080-exec-1] c.l.s.service.InboundApiAuditService     : Inbound API request audit: AuditRequestDto(traceId=null, serviceName=INBOUND_API, requestUrl=http://localhost:8080/learning/topic, requestMethod=GET, payload=null, requestTimestamp=Tue Oct 27 00:01:34 IST 2026)
2026-10-27T00:01:34.935+05:30  INFO 32325 --- [springsecurity] [nio-8080-exec-1] c.l.s.filters.TraceIdFilter              : Executing TraceIdFilter to set the traceId : cf6837d2-934b-470a-8c4d-0e69801606bd
2026-10-27T00:01:34.957+05:30  INFO 32325 --- [springsecurity] [nio-8080-exec-1] c.l.s.service.InboundApiAuditService     : Inbound API response audit: AuditResponseDto(traceId=null, serviceName=INBOUND_API, requestUrl=http://localhost:8080/learning/topic, responseStatus=OK, response="Today we are implementing Inbound/Outbound audit system", responseTimestamp=Tue Oct 27 00:01:34 IST 2026)
````
- Here, traceId are null for InboundApiAudit logs, why ?: as I added InboundApiAuditFilter first and then TraceIdFilter so by default spring considers the same order for the execution.

How to fix this ? : use @Order(number) annotation, lower the number, higher the execution priority
- Observe the fixed response now !
````
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.1.2)

2026-10-27T00:09:25.524+05:30  INFO 32696 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Starting SpringsecurityApplication using Java 21.0.12.1 with PID 32696 (/Users/modin/Downloads/springsecurity/target/classes started by modin in /Users/modin/Downloads/springsecurity)
2026-10-27T00:09:25.526+05:30  INFO 32696 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : No active profile set, falling back to 1 default profile: "default"
2026-10-27T00:09:25.913+05:30  INFO 32696 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2026-10-27T00:09:25.921+05:30  INFO 32696 --- [springsecurity] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-10-27T00:09:25.921+05:30  INFO 32696 --- [springsecurity] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.27]
2026-10-27T00:09:25.942+05:30  INFO 32696 --- [springsecurity] [           main] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 388 ms
2026-10-27T00:09:26.101+05:30  INFO 32696 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-10-27T00:09:26.104+05:30  INFO 32696 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Started SpringsecurityApplication in 0.803 seconds (process running for 1.005)
2026-10-27T00:09:32.937+05:30  INFO 32696 --- [springsecurity] [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2026-10-27T00:09:32.937+05:30  INFO 32696 --- [springsecurity] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2026-10-27T00:09:32.938+05:30  INFO 32696 --- [springsecurity] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-10-27T00:09:32.941+05:30  INFO 32696 --- [springsecurity] [nio-8080-exec-1] c.l.s.filters.TraceIdFilter              : Executing TraceIdFilter to set the traceId : f38dbff1-09bb-4c69-85ac-86b396e486a1
2026-10-27T00:09:32.945+05:30  INFO 32696 --- [springsecurity] [nio-8080-exec-1] c.l.s.service.InboundApiAuditService     : Inbound API request audit: AuditRequestDto(traceId=f38dbff1-09bb-4c69-85ac-86b396e486a1, serviceName=INBOUND_API, requestUrl=http://localhost:8080/learning/topic, requestMethod=GET, payload=null, requestTimestamp=Tue Oct 27 00:09:32 IST 2026)
2026-10-27T00:09:32.982+05:30  INFO 32696 --- [springsecurity] [nio-8080-exec-1] c.l.s.service.InboundApiAuditService     : Inbound API response audit: AuditResponseDto(traceId=f38dbff1-09bb-4c69-85ac-86b396e486a1, serviceName=INBOUND_API, requestUrl=http://localhost:8080/learning/topic, responseStatus=OK, response="Today we are implementing Inbound/Outbound audit system", responseTimestamp=Tue Oct 27 00:09:32 IST 2026)
````
