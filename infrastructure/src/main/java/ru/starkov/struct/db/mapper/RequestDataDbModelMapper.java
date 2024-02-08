package ru.starkov.struct.db.mapper;

import org.springframework.stereotype.Component;
import ru.starkov.struct.db.model.VoiceRequestDataDbModel;
import ru.starkov.struct.db.model.RequestDataDbModel;
import ru.starkov.struct.db.model.TextRequestDataDbModel;
import ru.starkov.dom.entity.RequestData;
import ru.starkov.dom.entity.identifier.RequestDataId;

@Component
public class RequestDataDbModelMapper {

    public RequestData<?> toEntity(RequestDataDbModel<?> model) {

        var id = new RequestDataId(model.getId());
        var data = model.getData();
        if (model instanceof TextRequestDataDbModel textRequestDataDbModel) {
            return RequestData.createNewInstance(
                    id,
                    data,
                    RequestData.RequestDataType.TEXT,
                    textRequestDataDbModel.getPrompt(),
                    null,
                    null);
        } else if (model instanceof VoiceRequestDataDbModel voiceRequestDataDbModel) {
            return RequestData.createNewInstance(
                    id,
                    data,
                    RequestData.RequestDataType.VOICE,
                    null,
                    voiceRequestDataDbModel.getRecognizedVoiceRequest(),
                    voiceRequestDataDbModel.getVoiceRequestDuration());
        } else {
            throw new RuntimeException();
        }
    }

    public RequestDataDbModel<?> toModel(RequestData<?> entity) {
        var id = entity.getRequestDataId().id();
        var data = entity.getData();
        var dataType = entity.getDataType().name();
        if (data instanceof String stringData) {
            TextRequestDataDbModel model = new TextRequestDataDbModel();
            model.setId(id);
            model.setData(stringData);
            model.setDataType(dataType);
            if (entity.getPrompt() != null) {
                model.setPrompt(entity.getPrompt());
            }
            return model;
        } else if (data instanceof byte[] byteData) {
            VoiceRequestDataDbModel model = new VoiceRequestDataDbModel();
            model.setId(id);
            model.setData(byteData);
            model.setDataType(dataType);
            if (entity.getRecognizedVoiceRequest() != null) {
                model.setRecognizedVoiceRequest(entity.getRecognizedVoiceRequest());
            }
            if (entity.getVoiceRequestDurationInSeconds() != null) {
                model.setVoiceRequestDuration(entity.getVoiceRequestDurationInSeconds());
            }
            return model;
        }
        throw new RuntimeException("Couldn't map entity to model. Unsupported data type: " + data.getClass().getSimpleName());
    }
}
