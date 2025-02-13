package ru.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class Link {

    private String longLink;
    private String shortLink;
    private UUID userUUID;
    private int usesLeft;
    private LocalDateTime expirationDate;
}
