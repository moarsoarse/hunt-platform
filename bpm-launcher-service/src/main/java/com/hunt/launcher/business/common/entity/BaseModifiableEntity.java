package com.hunt.bpm-launcher-service.business.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModifiableEntity<T_ID> extends BaseEntity<T_ID> {

    private LocalDateTime modified;
    
    @PrePersist
    @PreUpdate
    public void fillModified() {
        modified = LocalDateTime.now();
    }
}
