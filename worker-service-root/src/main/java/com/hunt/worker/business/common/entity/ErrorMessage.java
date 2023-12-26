package com.hunt.worker-service-root.business.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@ToString
public class ErrorMessage {

    private String message;

    private String description;

    private String stackTrace;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime;

    private List<String> violations;

    @Tolerate
    public ErrorMessage() {

    }
}
