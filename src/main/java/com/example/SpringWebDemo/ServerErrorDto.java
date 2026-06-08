package com.example.SpringWebDemo;

import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {
}
