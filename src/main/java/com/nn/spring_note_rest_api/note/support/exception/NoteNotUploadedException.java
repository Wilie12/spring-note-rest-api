package com.nn.spring_note_rest_api.note.support.exception;

public class NoteNotUploadedException extends RuntimeException {
    public NoteNotUploadedException(String filename) {
        super("Note not uploaded: " + filename);
    }
}
