package com.learnings.springsecurity.filters;

import java.io.IOException;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.learnings.springsecurity.constants.Constants.TRACE_ID_KEY;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
@Order(0) //this defines the order of the execution, lower the number, higher is the priority
public class TraceIdFilter extends OncePerRequestFilter {
    private static final String TRACE_ID = UUID.randomUUID().toString();

    //This filter will assign a traceId to the workflow which is used in debugging
    //MDC is a map that is shared across all threads

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDC.put(TRACE_ID_KEY, TRACE_ID);
            log.info("Executing TraceIdFilter to set the traceId : {}", TRACE_ID);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }
}
