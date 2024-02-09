package ru.starkov.dom.entity;

import ru.starkov.common.event.ResultWithEvent;
import ru.starkov.dom.entity.identifier.CustomerRequestIdGenerator;
import ru.starkov.dom.event.CustomerRequestCreatedEvent;
import ru.starkov.dom.event.CustomerRequestHandledByGptEvent;
import ru.starkov.dom.event.CustomerRequestRecognizedToTextEvent;
import ru.starkov.dom.event.ResultsSentToCustomerEvent;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import lombok.*;
import ru.starkov.common.mark.Entity;
import ru.starkov.dom.value.ResultData;

import java.math.BigInteger;
import java.util.Objects;

import static ru.starkov.dom.entity.Customer.State.ACTIVE;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class CustomerRequest implements Entity {

    private final CustomerRequestId customerRequestId;
    private final Customer customer;
    private final RequestData<?> requestData;

    private State state;
    private ResultData resultData;
    private BigInteger tokenCost;


    public static ResultWithEvent<CustomerRequest, CustomerRequestCreatedEvent> create(
            @NonNull CustomerRequestIdGenerator idGenerator,
            @NonNull Customer customer,
            @NonNull RequestData<?> requestData) {

        Objects.requireNonNull(idGenerator, "ID Generator cannot be null");
        Objects.requireNonNull(customer, "Customer cannot be null");
        Objects.requireNonNull(requestData, "Request Data cannot be null");

        if (customer.getState() != ACTIVE) {
            throw new RuntimeException("Cannot create request for inactive customer");
        }

        var customerRequest = new CustomerRequest(idGenerator.generate(), customer, requestData);
        customerRequest.state = State.CREATED;
        return ResultWithEvent.of(customerRequest, new CustomerRequestCreatedEvent(customerRequest.customerRequestId));
    }

    private CustomerRequest(CustomerRequestId customerRequestId, Customer customer, RequestData<?> requestData) {
        this.customerRequestId = customerRequestId;
        this.customer = customer;
        this.requestData = requestData;
    }

    public ResultWithEvent<CustomerRequest, CustomerRequestRecognizedToTextEvent> recognize(
            @NonNull String recognizedText) {

        Objects.requireNonNull(recognizedText);

        if (this.state != State.CREATED) {
            throw new RuntimeException("Failed to recognize: Invalid state " + this.state);
        }

        this.state = State.HANDLED;



        this.resultData = new ResultData(recognizedText);
        return ResultWithEvent.of(this, new CustomerRequestRecognizedToTextEvent(this.customerRequestId));
    }

    public ResultWithEvent<CustomerRequest, CustomerRequestHandledByGptEvent> handleByGpt(
            @NonNull String handledText,
            @NonNull BigInteger tokenCost) {

        Objects.requireNonNull(handledText, "Handled Text cannot be null");

        if (this.state != State.CREATED) {
            throw new RuntimeException("Failed to handle by GPT: Invalid state " + this.state);
        }

        this.state = State.HANDLED;
        this.resultData = new ResultData(handledText);
        this.tokenCost = tokenCost;
        return ResultWithEvent.of(this, new CustomerRequestHandledByGptEvent(this.customerRequestId));
    }

    public ResultWithEvent<CustomerRequest, ResultsSentToCustomerEvent> sendResultsToCustomer() {
        if (this.state != State.HANDLED) {
            throw new RuntimeException("Cannot send results to customer, text is not handled by GPT yet");
        }

        this.state = State.COMPLETED;
        return ResultWithEvent.of(this, new ResultsSentToCustomerEvent(this.customerRequestId));
    }

    public enum State {
        CREATED,
        HANDLED,
        COMPLETED,
    }
}