package ru.starkov.app.dto.out.gpt_request;

public record GptPromptRequest(String request, String prompt, String gptModelUrl) {
}
