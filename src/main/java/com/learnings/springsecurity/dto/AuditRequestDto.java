package com.learnings.springsecurity.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.JsonNode;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuditRequestDto {

    private String traceId;
    private String serviceName;
    private String requestUrl;
    private String requestMethod;
    private JsonNode payload;
    private Date requestTimestamp;
}
