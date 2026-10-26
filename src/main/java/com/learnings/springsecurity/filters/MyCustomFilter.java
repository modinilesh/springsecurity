package com.learnings.springsecurity.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class MyCustomFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(MyCustomFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Custom logic for your own filter
        logger.info("Request with URL: {} came at filter at :{}", ((HttpServletRequest) request).getRequestURL(), new Date());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
