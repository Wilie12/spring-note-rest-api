package com.nn.spring_note_rest_api.note.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteMetaDataRepository extends JpaRepository<NoteMetadata, Long> {
}
