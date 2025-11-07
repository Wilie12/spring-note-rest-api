package com.nn.spring_note_rest_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nn.spring_note_rest_api.language.api.response.LanguageResponse;
import com.nn.spring_note_rest_api.language.domain.LanguageMatch;
import com.nn.spring_note_rest_api.language.domain.Replacement;
import com.nn.spring_note_rest_api.language.service.LanguageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(LanguageService.class)
@TestPropertySource(properties = "api.host.baseurl=http://localhost:8010/v2/check")
public class LanguageServiceTest {
    @Autowired
    private MockRestServiceServer mockRestServiceServer;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${api.host.baseurl}")
    private String BASE_URL;

    @Test
    public void checkGrammarShouldWork() throws Exception {
        // given
        LanguageResponse mockResponse = new LanguageResponse(
          List.of(
                  new LanguageMatch(
                          "Test message",
                          List.of(new Replacement("replacement")),
                          0,
                          11,
                          "Test sentence"
                  )
          )
        );

        String fileContent = "Test file content.";
        Resource resource = new ByteArrayResource(fileContent.getBytes());
        String responseBody = objectMapper.writeValueAsString(mockResponse);
        URI executedUri = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("language", "auto")
                .queryParam("text", fileContent)
                .build()
                .toUri();

        mockRestServiceServer.expect(requestTo(executedUri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        // when
        LanguageResponse actualResponse = languageService.checkGrammar(resource);

        // then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.matches()).hasSize(1);
        assertThat(actualResponse.matches().get(0).message()).isEqualTo("Test message");
        assertThat(actualResponse.matches().get(0).offset()).isEqualTo(0);
        assertThat(actualResponse.matches().get(0).length()).isEqualTo(11);
        assertThat(actualResponse.matches().get(0).sentence()).isEqualTo("Test sentence");

        mockRestServiceServer.verify();
    }
}
