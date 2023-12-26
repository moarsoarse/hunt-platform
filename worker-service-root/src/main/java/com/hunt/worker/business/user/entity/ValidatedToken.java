package com.hunt.worker-service-root.business.user.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidatedToken {

    private String subject;

    private String token;
}
