package com.hubi.todoapp.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
public class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    public void prePersisit(){
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        updatedOn = LocalDateTime.now();
    }
}
