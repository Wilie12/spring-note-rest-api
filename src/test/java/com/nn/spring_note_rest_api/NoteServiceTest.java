package com.nn.spring_note_rest_api;

import com.nn.spring_note_rest_api.note.api.response.NoteResponse;
import com.nn.spring_note_rest_api.note.config.NoteStorageProperties;
import com.nn.spring_note_rest_api.note.repository.NoteMetaDataRepository;
import com.nn.spring_note_rest_api.note.domain.NoteMetadata;
import com.nn.spring_note_rest_api.note.service.LocalNoteStorageService;
import com.nn.spring_note_rest_api.note.service.NoteService;
import com.nn.spring_note_rest_api.note.support.NoteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    private NoteStorageProperties noteStorageProperties;
    @Mock
    NoteMetaDataRepository noteMetaDataRepository;
    @Mock
    LocalNoteStorageService localNoteStorageService;
    @Mock
    NoteMapper noteMapper;
    private NoteService noteService;

    @BeforeEach
    public void setUp() {

        noteService = new NoteService(
                noteStorageProperties,
                noteMetaDataRepository,
                localNoteStorageService,
                noteMapper
        );
    }

    @Test
    public void uploadNoteShouldWork() throws IOException {
        // given
        String name = "file";
        String content = "Test file content.";
        String originalName = "test.txt";
        String contentType = "text/plain";

        MultipartFile multipartFile = new MockMultipartFile(
                name,
                originalName,
                contentType,
                content.getBytes()
        );
        NoteMetadata noteMetadata = new NoteMetadata(
                originalName,
                "storedPath",
                contentType,
                2L
        );
        NoteResponse noteResponse = new NoteResponse(
                1L,
                originalName,
                2L
        );

        when(noteStorageProperties.allowedMimeTypes()).thenReturn(Set.of("text/plain"));
        when(localNoteStorageService.storeFile(any(), any())).thenReturn("storedPath");
        when(noteMetaDataRepository.save(any())).thenReturn(noteMetadata);
        when(noteMapper.toNoteResponse(any())).thenReturn(noteResponse);

        // when
        NoteResponse actualNoteResponse = noteService.uploadNote(multipartFile);

        // then
        assertThat(actualNoteResponse.noteId()).isEqualTo(1L);
        assertThat(actualNoteResponse.originalName()).isEqualTo(originalName);
        assertThat(actualNoteResponse.size()).isEqualTo(2L);
    }

    @Test
    public void getNoteMetadataShouldWork() {
        // given
        NoteMetadata noteMetadata = new NoteMetadata(
                "test.txt",
                "storedPath",
                "text/plain",
                2L
        );
        when(noteMetaDataRepository.findById(any())).thenReturn(Optional.of(noteMetadata));

        // when
        NoteMetadata actualNoteMetadata = noteService.getNoteMetadata(1L);

        // then
        assertThat(actualNoteMetadata.getOriginalName()).isEqualTo("test.txt");
        assertThat(actualNoteMetadata.getSize()).isEqualTo(2L);
        assertThat(actualNoteMetadata.getMimeType()).isEqualTo("text/plain");
        assertThat(actualNoteMetadata.getStoredName()).isEqualTo("storedPath");
    }

    @Test
    public void getNoteResourceShouldWork() throws IOException {
        // given
        NoteMetadata noteMetadata = new NoteMetadata(
                "test.txt",
                "storedPath",
                "text/plain",
                2L
        );
        String fileContent = "Test file content.";
        Resource resource = new ByteArrayResource(fileContent.getBytes());

        when(noteMetaDataRepository.findById(any())).thenReturn(Optional.of(noteMetadata));
        when(localNoteStorageService.getFileResource(any())).thenReturn(resource);

        // when
        Resource actualResource = noteService.getNoteResource(1L);

        // then
        assertThat(actualResource.getContentAsString(StandardCharsets.UTF_8)).isEqualTo(fileContent);
    }
}
