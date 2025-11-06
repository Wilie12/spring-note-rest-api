package com.nn.spring_note_rest_api.note.controller;

import com.nn.spring_note_rest_api.language.api.response.LanguageResponse;
import com.nn.spring_note_rest_api.language.service.LanguageService;
import com.nn.spring_note_rest_api.note.api.response.NoteResponse;
import com.nn.spring_note_rest_api.note.domain.NoteMetadata;
import com.nn.spring_note_rest_api.note.service.MarkdownService;
import com.nn.spring_note_rest_api.note.service.NoteService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final LanguageService languageService;
    private final MarkdownService markdownService;

    public NoteController(
            NoteService noteService,
            LanguageService languageService,
            MarkdownService markdownService
    ) {
        this.noteService = noteService;
        this.languageService = languageService;
        this.markdownService = markdownService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<NoteResponse> uploadNote(@RequestParam("file") MultipartFile file) {
        NoteResponse noteResponse = noteService.uploadNote(file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteResponse);
    }

    @GetMapping("/check/{noteId}")
    @ResponseBody
    public ResponseEntity<LanguageResponse> checkNoteGrammar(@PathVariable long noteId) {
        Resource note = noteService.getNoteResource(noteId);

        LanguageResponse languageResponse = languageService.checkGrammar(note);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(languageResponse);
    }

    @GetMapping("/{noteId}")
    public String showNote(@PathVariable long noteId, Model model) {
        Resource note = noteService.getNoteResource(noteId);
        NoteMetadata noteMetadata = noteService.getNoteMetadata(noteId);
        String html = markdownService.resourceToHtml(note);

        model.addAttribute("title", noteMetadata.getOriginalName());
        model.addAttribute("markdown", html);
        return "note";
    }
}
