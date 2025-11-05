package com.nn.spring_note_rest_api.language.domain;

import java.util.List;

public record LanguageMatch(
        String message,
        List<Replacement> replacements,
        int offset,
        int length,
        String sentence
) {
}
