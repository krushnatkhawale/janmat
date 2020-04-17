package com.bharat.janmat.clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

    public LocationClient(@Value("${location.client:https://freegeoip.app/json/}") String locationServiceURL) {
        this.restTemplate = new RestTemplate();
        this.locationServiceURL = locationServiceURL;
    }

    public String getLocation(final ServletRequest servletRequest) {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final String clientIp = getClientIp(httpServletRequest);
        if (isNull(locations.get(clientIp))) {
            final String url = locationServiceURL + clientIp;
            final ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(url, JsonNode.class);
            final HttpStatus statusCode = responseEntity.getStatusCode();

            if (statusCode.equals(HttpStatus.OK)) {
                final JsonNode resultNode = responseEntity.getBody();
                final String result = resultNode.get("city").asText();
                locations.put(clientIp, resultNode.toString());
                LOGGER.info("New ip request: {}: {}: {}", clientIp, ((HttpServletRequest) servletRequest).getRequestURL(), locations.get(clientIp));
                return "SUCCESS";
            } else {
                return "Unknown";
            }
        } else {
            LOGGER.info("Existing ip request: {}: {}: {}", clientIp, ((HttpServletRequest) servletRequest).getRequestURL(), locations.get(clientIp));
            return "Unknown";
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