package com.example.demo;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
public class DemoEntity {

    @Id
    private String id;

    private String title;

    private Instant modifiedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        modifiedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = Instant.now();
    }

    // getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Instant getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(Instant modifiedAt) { this.modifiedAt = modifiedAt; }
}