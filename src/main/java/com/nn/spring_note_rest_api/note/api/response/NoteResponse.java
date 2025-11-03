package com.nn.spring_note_rest_api.note.api.response;

public record NoteResponse(
        long noteId,
        String originalName,
        long size
) {
}
