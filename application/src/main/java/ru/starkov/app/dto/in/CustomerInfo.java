package ru.starkov.app.dto.in;


public record CustomerInfo(Long customerTelegramId, String firstName, String lastName, String languageCode) {
}
