package com.nn.spring_note_rest_api.language.service;

import com.nn.spring_note_rest_api.language.api.response.LanguageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class LanguageService {
    private final RestClient restClient;

    public LanguageService(
            RestClient.Builder builder,
            @Value("${api.host.baseurl}") String baseUrl
    ) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public LanguageResponse checkGrammar(Resource resource) {
        String resourceAsString;
        try {
            resourceAsString = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ResourceAccessException("Resource not found");
        }

        System.out.println("resourceAsString : " + resourceAsString);

        return restClient.post()
                .uri("?language=auto&text={resourceAsString}", resourceAsString)
                .retrieve()
                .body(LanguageResponse.class);
    }
}
