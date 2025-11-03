package com.nn.spring_note_rest_api.note.service;

import com.nn.spring_note_rest_api.note.config.NoteStorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
public class LocalNoteStorageService {
    private final NoteStorageProperties noteStorageProperties;
    private final Path rootPath;

    public LocalNoteStorageService(NoteStorageProperties noteStorageProperties) {
        this.noteStorageProperties = noteStorageProperties;
        rootPath = Paths.get(noteStorageProperties.basePath());
    }

    public String storeFile(InputStream inputStream, String originalName) throws IOException {
        Files.createDirectories(rootPath);

        String ext = getFileExtension(originalName);
        String storedName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path filePath = rootPath.resolve(storedName);

        try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
            StreamUtils.copy(inputStream, outputStream);
        }

        return rootPath.relativize(filePath).toString();
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }
}
