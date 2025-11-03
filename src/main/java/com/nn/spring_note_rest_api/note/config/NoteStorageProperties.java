package com.nn.spring_note_rest_api.note.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@ConfigurationProperties(prefix = "app.note-storage")
@Component
public record NoteStorageProperties(
        String basePath,
        Set<String> allowedMimeTypes
) {
    public NoteStorageProperties() {
        this("./notes", Set.of("text/plain"));
    }
}
