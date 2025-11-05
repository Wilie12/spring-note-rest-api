package com.nn.spring_note_rest_api.language.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class LanguageToolConfig {
    @Value("${api.host.baseurl}")
    String baseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.create(baseUrl);
    }
}
