package ru.starkov.dom.value;

import lombok.*;
import ru.starkov.common.mark.ValueObject;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class CustomerSettings implements ValueObject {


    private ActionType defaultActionType;
    private GptModel defaultGptModel;

    public static CustomerSettings createDefaultSettings() {
        return CustomerSettings.builder()
                .defaultActionType(ActionType.ASK_GPT)
                .defaultGptModel(GptModel.YANDEX_GPT)
                .build();
    }

    // used by mapper
    public static CustomerSettings createNewInstance(ActionType actionType, GptModel gptModel) {
        return CustomerSettings.builder()
                .defaultActionType(actionType)
                .defaultGptModel(gptModel)
                .build();
    }


    public CustomerSettings changeDefaultActionType(ActionType actionType) {
        this.setDefaultActionType(actionType);
        return this;
    }
    public CustomerSettings changeDefaultGptModel(GptModel gptModel) {
        this.setDefaultGptModel(gptModel);
        return this;
    }


    public enum ActionType {

        RECOGNIZE,
        SUMMARIZE,
        ASK_GPT,
        ASK_GPT_WITH_PROMPT
    }

    @Getter
    public enum GptModel {
        YANDEX_GPT("gpt://b1gqvj9jdr0pru8t82tr/yandexgpt"),
        YANDEX_GPT_LITE("gpt://b1gqvj9jdr0pru8t82tr/yandexgpt-lite");

        private static final Map<String, GptModel> MODEL_URL_TO_ENUM_MAP;

        static {
            MODEL_URL_TO_ENUM_MAP = Arrays.stream(GptModel.values())
                    .collect(Collectors.toMap(GptModel::getModelUrl, Function.identity()));
        }

        private final String modelUrl;

        GptModel(String modelUrl) {
            this.modelUrl = modelUrl;
        }

        public static GptModel fromModelUrl(String modelUrl) {
            GptModel model = MODEL_URL_TO_ENUM_MAP.get(modelUrl);
            if (model == null) {
                throw new IllegalArgumentException("No GptModel exists for the provided model url");
            }
            return model;
        }
    }
}
