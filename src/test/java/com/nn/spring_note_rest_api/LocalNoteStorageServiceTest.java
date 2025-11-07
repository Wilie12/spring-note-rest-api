package com.nn.spring_note_rest_api;

import com.nn.spring_note_rest_api.note.config.NoteStorageProperties;
import com.nn.spring_note_rest_api.note.service.LocalNoteStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocalNoteStorageServiceTest {
    @Mock
    private NoteStorageProperties noteStorageProperties;
    @TempDir
    private Path tempDir;
    private LocalNoteStorageService localNoteStorageService;

    @BeforeEach
    public void setUp() {
        when(noteStorageProperties.basePath()).thenReturn(tempDir.toString());
        localNoteStorageService = new LocalNoteStorageService(noteStorageProperties);
    }

    @Test
    public void storeFileShouldWork() throws IOException {
        // given
        String fileContent = "Test file content.";
        String originalName = "test.txt";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        // when
        String storedPath = localNoteStorageService.storeFile(inputStream, originalName);

        // then
        assertThat(storedPath).doesNotContain(originalName);
        Path expectedFilePath = tempDir.resolve(storedPath);
        assertThat(Files.exists(expectedFilePath)).isTrue();

        String actualContent = Files.readString(expectedFilePath);
        assertThat(actualContent).isEqualTo(fileContent);
    }
}
