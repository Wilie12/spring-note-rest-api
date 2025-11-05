package com.nn.spring_note_rest_api.language.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nn.spring_note_rest_api.language.domain.LanguageMatch;

import java.util.List;

@JsonIgnoreProperties
public record LanguageResponse(List<LanguageMatch> matches) {
}
