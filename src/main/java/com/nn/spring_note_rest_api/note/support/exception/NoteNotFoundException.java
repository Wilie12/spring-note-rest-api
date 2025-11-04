package com.nn.spring_note_rest_api.note.support.exception;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(long noteId) {
        super("Note with id " + noteId + " not found");
    }
}
