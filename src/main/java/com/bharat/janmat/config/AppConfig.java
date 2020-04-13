package com.bharat.janmat.config;

import com.bharat.janmat.util.DateAndTimeDeserializer;
import com.bharat.janmat.util.DateAndTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(ZonedDateTime.class, new DateAndTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new DateAndTimeDeserializer());

        objectMapper.registerModule(module);
        return objectMapper;
    }
}
