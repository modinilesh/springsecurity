package com.learnings.springsecurity.service;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public interface AuditService {

    void logRequest(ServletRequest request);

    void logResponse(String requestUrl, ServletResponse response);

    void logError(String requestUrl, ServletResponse response);
}
