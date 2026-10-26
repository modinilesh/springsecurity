package com.learnings.springsecurity.filters;

import com.learnings.springsecurity.service.InboundApiAuditService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Order(1)
public class InboundApiAuditFilter extends OncePerRequestFilter {

    @Autowired
    private InboundApiAuditService inboundApiAuditService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 1024 * 1024);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            inboundApiAuditService.logRequest(requestWrapper);
            filterChain.doFilter(requestWrapper, responseWrapper);
            inboundApiAuditService.logResponse(requestWrapper.getRequestURL().toString(), responseWrapper);
        } catch (ServletException | IOException | RuntimeException ex) {
            inboundApiAuditService.logError(requestWrapper.getRequestURL().toString(), responseWrapper);
            throw ex;
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }
}
