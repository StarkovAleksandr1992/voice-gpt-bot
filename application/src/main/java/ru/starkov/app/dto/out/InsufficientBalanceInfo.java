package ru.starkov.app.dto.out;

import java.math.BigInteger;

public record InsufficientBalanceInfo(Long customerTelegramId, BigInteger balance) {
}
