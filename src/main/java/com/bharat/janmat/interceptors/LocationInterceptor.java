package com.bharat.janmat.interceptors;

import com.bharat.janmat.clients.LocationClient;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class LocationInterceptor implements Filter {

    private final LocationClient locationClient;

    public LocationInterceptor(LocationClient locationClient) {
        this.locationClient = locationClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        locationClient.getLocation(request);
        chain.doFilter(request, response);
    }
}
