package ru.starkov.struct.yandex.stt;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;
import yandex.cloud.api.ai.stt.v3.RecognizerGrpc;
import yandex.cloud.api.ai.stt.v3.Stt;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SttV3Client {

    private final String host;
    private final int port;

    private final String apiKey;

    public SttV3Client(String host, int port, String apikey) {
        this.host = host;
        this.port = port;
        this.apiKey = apikey;
    }


    public String recognize(byte[] data) throws InterruptedException {
        var channel = ManagedChannelBuilder
                .forAddress(host, port)
                .build();

        var headers = new Metadata();
        headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "Api-Key " + apiKey);
        var requestId = UUID.randomUUID().toString();
        headers.put(Metadata.Key.of("x-client-request-id", Metadata.ASCII_STRING_MARSHALLER), requestId);

        var client = RecognizerGrpc.newStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers));


        var responseObserver = new SttStreamObserver();
        var requestObserver = client.recognizeStreaming(responseObserver);

        // Начало новой сессии
        requestObserver.onNext(request());

        // Отправка аудиофрагмента
        Stt.StreamingRequest.Builder reqB = Stt.StreamingRequest.newBuilder();
        reqB.getChunkBuilder().setData(ByteString.copyFrom(data));
        requestObserver.onNext(reqB.build());

        requestObserver.onCompleted();
        // Завершение сессии
        ManagedChannel shutdown = channel.shutdown();
        shutdown.awaitTermination(10, TimeUnit.SECONDS);
        return responseObserver.awaitResult();
    }

    private Stt.StreamingRequest request() {
        var builder = Stt.StreamingRequest.newBuilder();
        builder.getSessionOptionsBuilder()
                .setRecognitionModel(Stt.RecognitionModelOptions.newBuilder()
                        .setLanguageRestriction(Stt.LanguageRestrictionOptions.newBuilder()
                                .addLanguageCode("ru-RU")
                                .setRestrictionType(Stt.LanguageRestrictionOptions.LanguageRestrictionType.WHITELIST)
                                .build())
                        .setTextNormalization(Stt.TextNormalizationOptions.newBuilder()
                                .setLiteratureText(true)
                                .setTextNormalization(Stt.TextNormalizationOptions.TextNormalization.TEXT_NORMALIZATION_ENABLED)
                                .setProfanityFilter(false)
                                .setTextNormalizationValue(1)
                                .build())
                        .setAudioFormat(Stt.AudioFormatOptions.newBuilder()
                                .setContainerAudio(Stt.ContainerAudio.newBuilder()
                                        .setContainerAudioType(Stt.ContainerAudio.ContainerAudioType.OGG_OPUS)
                                        .build()))
                        .setAudioProcessingType(Stt.RecognitionModelOptions.AudioProcessingType.FULL_DATA)
                        .build());
        return builder.build();
    }


    static class SttStreamObserver implements StreamObserver<Stt.StreamingResponse> {

        private final StringBuilder result = new StringBuilder();

        private static final CountDownLatch count = new CountDownLatch(1);

        @Override
        public void onNext(Stt.StreamingResponse response) {
            response.getFinalRefinement()
                    .getNormalizedText()
                    .getAlternativesList()
                    .forEach(a -> result.append(a.getText().trim()).append(" "));
        }

        @Override
        public void onError(Throwable t) {
           throw new RecognizedProcessException(t);
        }

        @Override
        public void onCompleted() {
            count.countDown();
        }

        private String awaitResult() {
            try {
                count.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return result.toString();
        }
    }
}
