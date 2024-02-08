package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.app.port.EventPublisher;
import ru.starkov.app.port.SpeechToTextService;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.app.usecase.RecognizeCustomerRequestToText;


@Service
@RequiredArgsConstructor
public class RecognizeCustomerRequestToTextImpl implements RecognizeCustomerRequestToText {

    private final CustomerRequestGateWay customerRequestGateWay;

    private final SpeechToTextService speechToTextService;

    private final EventPublisher eventPublisher;


    @Override
    public void execute(CustomerRequestId customerRequestId) {
        var customerRequestOptional = customerRequestGateWay.findById(customerRequestId);
        if (customerRequestOptional.isEmpty()) {
            throw new RuntimeException("Customer request with ID: " + customerRequestId + " could not be found.");
        }
        var customerRequest = customerRequestOptional.get();
        var requestData = customerRequest.getRequestData();
        var recognizedText = speechToTextService.recognize((byte[]) requestData.getData());

        if (recognizedText == null) {
            throw new RuntimeException("Speech-to-text service failed to recognize audio message from the data of the request: " + customerRequestId);
        }
        var result = customerRequest.recognize(recognizedText);

        customerRequestGateWay.save(result.getValue());
        eventPublisher.publish(result.getEvent());
    }
}
