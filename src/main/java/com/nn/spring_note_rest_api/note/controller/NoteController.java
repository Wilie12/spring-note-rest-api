package com.nn.spring_note_rest_api.note.controller;

import com.nn.spring_note_rest_api.note.api.response.NoteResponse;
import com.nn.spring_note_rest_api.note.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> uploadNote(@RequestParam("file") MultipartFile file) {
        NoteResponse noteResponse = noteService.uploadNote(file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteResponse);
    }
}
