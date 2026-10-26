package com.learnings.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.JsonNode;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuditResponseDto {

    private String traceId;
    private String serviceName;
    private String requestUrl;
    private String responseStatus;
    private JsonNode response;
    private Date responseTimestamp;

}
