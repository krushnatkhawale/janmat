package com.bharat.janmat.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class LocationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationClient.class);
    private static final HashMap<String, String> locations = new HashMap<>();
    private RestTemplate restTemplate;
    private String locationServiceURL;
    private ObjectMapper objectMapper;

    public LocationClient(@Value("${location.service:http://localhost:9091/processLocation}") String locationServiceURL, final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
        this.locationServiceURL = locationServiceURL;
    }

    public void process(final ServletRequest servletRequest) {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final String clientIp = getClientIp(httpServletRequest);
        final String requestURL = httpServletRequest.getRequestURL().toString();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("ip", clientIp);
        objectNode.put("url", requestURL);

        try {
            final ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(locationServiceURL, objectNode, JsonNode.class);
            final HttpStatus statusCode = responseEntity.getStatusCode();

            if (!statusCode.equals(HttpStatus.OK)) {
                LOGGER.warn("Location service response was: {}: might be down, please check: {}", statusCode.value(), locationServiceURL);
            }
        } catch (ResourceAccessException e) {
            LOGGER.error("Unable to call location service: {}, Certificate issue: {}", locationServiceURL, e.getMessage());
        }
    }

    private static String getClientIp(HttpServletRequest httpServletRequest) {
        String remoteAddr = "";
        if (!isNull(httpServletRequest)) {
            remoteAddr = getForwardedFor(httpServletRequest);
            if (isEmpty(remoteAddr)) {
                remoteAddr = getRealIp(httpServletRequest);
                if (isEmpty(remoteAddr)) {
                    remoteAddr = httpServletRequest.getRemoteAddr();
                }
            } else {
                return remoteAddr;
            }
        }
        return remoteAddr;
    }


    private static String getForwardedFor(HttpServletRequest request) {
        return request.getHeader("X-FORWARDED-FOR");
    }

    private static String getRealIp(HttpServletRequest request) {
        return request.getHeader("X-REAL-IP");
    }
}