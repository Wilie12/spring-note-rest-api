package com.nn.spring_note_rest_api.note.controller;

import com.nn.spring_note_rest_api.language.api.response.LanguageResponse;
import com.nn.spring_note_rest_api.language.service.LanguageService;
import com.nn.spring_note_rest_api.note.api.response.NoteResponse;
import com.nn.spring_note_rest_api.note.service.NoteService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final LanguageService languageService;

    public NoteController(NoteService noteService, LanguageService languageService) {
        this.noteService = noteService;
        this.languageService = languageService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> uploadNote(@RequestParam("file") MultipartFile file) {
        NoteResponse noteResponse = noteService.uploadNote(file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteResponse);
    }

    @GetMapping("/check/{noteId}")
    public ResponseEntity<LanguageResponse> checkNoteGrammar(@PathVariable long noteId) {
        Resource note = noteService.getNoteResource(noteId);

        LanguageResponse languageResponse = languageService.checkGrammar(note);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(languageResponse);
    }
}
