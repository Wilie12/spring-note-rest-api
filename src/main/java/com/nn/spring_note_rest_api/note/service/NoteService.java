package com.nn.spring_note_rest_api.note.service;

import com.nn.spring_note_rest_api.note.api.response.NoteResponse;
import com.nn.spring_note_rest_api.note.config.NoteStorageProperties;
import com.nn.spring_note_rest_api.note.domain.NoteMetaDataRepository;
import com.nn.spring_note_rest_api.note.domain.NoteMetadata;
import com.nn.spring_note_rest_api.note.support.NoteMapper;
import com.nn.spring_note_rest_api.note.support.exception.NoteNotUploadedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class NoteService {
    private final NoteStorageProperties noteStorageProperties;
    private final NoteMetaDataRepository noteMetaDataRepository;
    private final LocalNoteStorageService localNoteStorageService;
    private final NoteMapper noteMapper;

    public NoteService(
            NoteStorageProperties noteStorageProperties,
            NoteMetaDataRepository noteMetaDataRepository,
            LocalNoteStorageService localNoteStorageService,
            NoteMapper noteMapper
    ) {
        this.noteStorageProperties = noteStorageProperties;
        this.noteMetaDataRepository = noteMetaDataRepository;
        this.localNoteStorageService = localNoteStorageService;
        this.noteMapper = noteMapper;
    }

    public NoteResponse uploadNote(MultipartFile file) {
        validateNote(file);

        String storagePath;
        try (InputStream inputStream = file.getInputStream()) {
            storagePath = localNoteStorageService.storeFile(inputStream, file.getOriginalFilename());
        } catch (IOException e) {
            throw new NoteNotUploadedException(file.getOriginalFilename());
        }

        NoteMetadata metadata = new NoteMetadata();
        metadata.setOriginalName(file.getOriginalFilename());
        metadata.setStoredName(storagePath);
        metadata.setMimeType(file.getContentType());
        metadata.setSize(file.getSize());

        NoteMetadata savedNoteMetaData = noteMetaDataRepository.save(metadata);
        return noteMapper.toNoteResponse(savedNoteMetaData);
    }

    private void validateNote(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !noteStorageProperties.allowedMimeTypes().contains(mimeType)) {
            throw new IllegalArgumentException("Invalid mime type: " + mimeType);
        }
    }
}
