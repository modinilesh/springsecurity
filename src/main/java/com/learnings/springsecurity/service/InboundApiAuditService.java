package com.learnings.springsecurity.service;

import com.learnings.springsecurity.dto.AuditRequestDto;
import com.learnings.springsecurity.dto.AuditResponseDto;
import com.learnings.springsecurity.enums.ApiAuditType;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.learnings.springsecurity.constants.Constants.TRACE_ID_KEY;

@Service
@Slf4j
public class InboundApiAuditService implements AuditService {
    private final ObjectMapper objectMapper;

    public InboundApiAuditService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void logRequest(ServletRequest request) {
        AuditRequestDto auditRequestDto = mapToAuditRequestDto(request);

        //Now, this dto can be handled in different ways
            //if system do not have high traffic -> directly store in db in appropriate request_audit table -> [Not recommended]
            //if system has high traffic and it's under microservice environment then keep audit services in "common" service
                //and @Import these service and use their bean to proceed
        //[Drawback] -> we are still auditing at the service level which is synchronous and might affect the latency, to avoid this use the brokers

        //Updated workflow "Event driven systems"
        //Client -> [server1] -> publishes requestTopic and responseTopic to message broker
        //          [Broker "rabbitMQ"/"kafka"] -> [audit-service] continuously listens to those topics and commits the data in db with appropriate masking strategy

        log.info("Inbound API request audit: {}", auditRequestDto);
    }

    @Override
    public void logResponse(String requestUrl, ServletResponse response) {
        if (!isRequestSucceed(response)) {
            logError(requestUrl, response);
            return;
        }
        AuditResponseDto auditResponseDto = mapToAuditResponseDto(requestUrl, response);
        log.info("Inbound API response audit: {}", auditResponseDto);
    }

    @Override
    public void logError(String requestUrl, ServletResponse response) {
        AuditResponseDto auditResponseDto = mapToAuditResponseDto(requestUrl, response);
        log.error("Inbound API error audit: {}", auditResponseDto);
    }

    private AuditRequestDto mapToAuditRequestDto(ServletRequest request) {
        AuditRequestDto requestDto = new AuditRequestDto();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        requestDto.setTraceId(MDC.get(TRACE_ID_KEY));
        requestDto.setServiceName(ApiAuditType.INBOUND_API.name());
        requestDto.setRequestUrl(httpServletRequest.getRequestURL().toString());
        requestDto.setRequestMethod(httpServletRequest.getMethod());
        byte[] body = request instanceof ContentCachingRequestWrapper wrapper
                ? wrapper.getContentAsByteArray()
                : new byte[0];
        requestDto.setPayload(toJsonNode(body, httpServletRequest.getCharacterEncoding()));
        requestDto.setRequestTimestamp(new Date());
        return requestDto;
    }

    private AuditResponseDto mapToAuditResponseDto(String requestUrl, ServletResponse response) {
        AuditResponseDto responseDto = new AuditResponseDto();
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        responseDto.setTraceId(MDC.get(TRACE_ID_KEY));
        responseDto.setServiceName(ApiAuditType.INBOUND_API.name());
        HttpStatus status = HttpStatus.resolve(servletResponse.getStatus());
        responseDto.setResponseStatus(status != null ? status.name() : String.valueOf(servletResponse.getStatus()));
        byte[] body = response instanceof ContentCachingResponseWrapper wrapper
                ? wrapper.getContentAsByteArray()
                : new byte[0];
        responseDto.setResponse(toJsonNode(body, servletResponse.getCharacterEncoding()));
        responseDto.setRequestUrl(requestUrl);
        responseDto.setResponseTimestamp(new Date());
        return responseDto;
    }

    private JsonNode toJsonNode(byte[] body, String encoding) {
        if (body == null || body.length == 0) {
            return objectMapper.nullNode();
        }
        try {
            return objectMapper.readTree(body);
        } catch (JacksonException ex) {
            Charset charset = encoding != null ? Charset.forName(encoding) : StandardCharsets.UTF_8;
            return objectMapper.getNodeFactory().textNode(new String(body, charset));
        }
    }

    private boolean isRequestSucceed(ServletResponse response) {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        return servletResponse.getStatus() >= 200 && servletResponse.getStatus() < 300;
    }
}
