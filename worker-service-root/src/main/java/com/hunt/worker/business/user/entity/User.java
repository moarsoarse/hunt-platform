package com.hunt.worker-service-root.business.user.entity;

import com.hunt.worker-service-root.business.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class User extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    private Long expirationTimeInMinutes;

    @Tolerate
    public User() {
    }
}
