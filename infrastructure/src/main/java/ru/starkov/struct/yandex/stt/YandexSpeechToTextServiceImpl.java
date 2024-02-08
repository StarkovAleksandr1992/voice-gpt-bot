package ru.starkov.struct.yandex.stt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.starkov.app.port.SpeechToTextService;


@Component
@RequiredArgsConstructor
public class YandexSpeechToTextServiceImpl implements SpeechToTextService {

    private final SttV3Client sttV3Client;

    @Override
    public String recognize(byte[] voiceMessageData) {
        String recognize;
        int attempts = 3;
        for (int i = 0; i < attempts; i++) {
            try {
                recognize = sttV3Client.recognize(voiceMessageData);
                if (!recognize.isBlank()) {
                    return recognize;
                }
            } catch (InterruptedException e) {
                //ignore
            }
        }
        throw new RuntimeException("Все попытки выполнить перевод речи в текст были неуспешными или возвращают пустую строку");
    }
}