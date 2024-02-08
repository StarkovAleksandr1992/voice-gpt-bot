package ru.starkov.struct.yandex.stt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeechKitClientConfig {

    @Value("${infrastructure.yandex.speech-kit.host}")
    private String host;
    @Value("${infrastructure.yandex.speech-kit.port}")
    private int port;
    @Value("${infrastructure.yandex.speech-kit.api-key}")
    private String apiKey;

    @Bean
    public SttV3Client sttV3Client() {
        return new SttV3Client(host, port, apiKey);
    }

}
