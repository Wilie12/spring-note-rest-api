package com.nn.spring_note_rest_api.note.support;

import com.nn.spring_note_rest_api.note.api.response.NoteResponse;
import com.nn.spring_note_rest_api.note.domain.NoteMetadata;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public NoteResponse toNoteResponse(NoteMetadata noteMetadata) {
        return new NoteResponse(
                noteMetadata.getId(),
                noteMetadata.getOriginalName(),
                noteMetadata.getSize()
        );
    }
}
