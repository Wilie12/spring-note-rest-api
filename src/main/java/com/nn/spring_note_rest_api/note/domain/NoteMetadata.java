package com.nn.spring_note_rest_api.note.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity(name = "note_metadata")
public class NoteMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String originalName;
    String storedName;
    String mimeType;
    long size;
    @CreationTimestamp
    Instant createdAt;

    public NoteMetadata() {}

    public NoteMetadata(String originalName, String storedName, String mimeType, long size) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.mimeType = mimeType;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

