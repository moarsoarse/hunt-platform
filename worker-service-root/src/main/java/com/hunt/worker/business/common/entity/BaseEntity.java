package com.hunt.worker-service-root.business.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity<T_ID> implements Serializable {

    @NotNull
    protected LocalDateTime created;

    public abstract T_ID getId();

    @PrePersist
    public void fillCreated() {
        if (created == null) {
            created = LocalDateTime.now();
        }
    }

    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }
}
