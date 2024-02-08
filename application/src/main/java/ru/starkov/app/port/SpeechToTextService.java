package ru.starkov.app.port;

public interface SpeechToTextService {
    String recognize(byte[] voiceMessageData);
}
