package com.hunt.worker-service-root;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TestBuilderConfiguration {

    public static final TestBuilderConfiguration CLEARED_AND_SAVED = TestBuilderConfiguration.builder()
        .clear(true)
        .saved(true)
        .build();

    public static final TestBuilderConfiguration CLEARED = TestBuilderConfiguration.builder().clear(true).build();

    public static final TestBuilderConfiguration SAVED = TestBuilderConfiguration.builder().saved(true).build();

    private boolean saved;

    private boolean clear;
}
