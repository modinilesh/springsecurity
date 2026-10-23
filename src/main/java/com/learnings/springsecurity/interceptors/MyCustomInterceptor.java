package com.learnings.springsecurity.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Component
public class MyCustomInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MyCustomInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //Before each controller method, request comes to here
        //We can add our custom logic for any explicit permission check for the user
        //If this returns false then request does not reach to the controller
        log.info("Request with URL: {} came to interceptor (preHandle) layer at {}", request.getRequestURL(), new Date());
        log.info("Logging response status just before controller method invocation, status: {}", response.getStatus());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        log.info("Request with URL: {} came to interceptor (postHandle) layer at {}", request.getRequestURL(), new Date());
        //This can be used if response needs to be modified before returning back
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        log.info("Request with URL: {} came to interceptor (afterCompletion) layer at {}", request.getRequestURL(), new Date());
        //This can be used for resource cleanup and logging purposes
    }
}
