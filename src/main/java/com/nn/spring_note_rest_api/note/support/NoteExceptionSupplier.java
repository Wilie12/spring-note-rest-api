package com.nn.spring_note_rest_api.note.support;


import com.nn.spring_note_rest_api.note.support.exception.NoteNotFoundException;

import java.util.function.Supplier;

public class NoteExceptionSupplier {

    public static Supplier<NoteNotFoundException> noteNotFound(Long noteId) {
        return () -> new NoteNotFoundException(noteId);
    }
}
