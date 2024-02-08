package ru.starkov.app.dto.in.gpt_response;

import java.math.BigInteger;

public record GptResponseInfo(BigInteger totalTokens, String gptResponse) {
}
