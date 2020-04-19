package com.bharat.janmat.interceptors;

import com.bharat.janmat.clients.LocationClient;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

@Component
public class LocationInterceptor implements Filter {

    private final LocationClient locationClient;
    private final List<String> validPaths;

    public LocationInterceptor(LocationClient locationClient) {
        this.locationClient = locationClient;
        this.validPaths = asList("", "images/under-construction.jpg", "home", "postmat", "favicon.ico", "css/home.css", "images/people-flag.png");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        locationClient.process(request);
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        String subUrl = url.substring(url.lastIndexOf(":"));
        String path = subUrl.substring(subUrl.indexOf("/") + 1);
        boolean validRequest = false;
        for (String validPath : validPaths) {
            if (path.equalsIgnoreCase(validPath)) {
                validRequest = true;
                break;
            }
        }
        if (validRequest) {
            chain.doFilter(request, response);
        }
    }
}
