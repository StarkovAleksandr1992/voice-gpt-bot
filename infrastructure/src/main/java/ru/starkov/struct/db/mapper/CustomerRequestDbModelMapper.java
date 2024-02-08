package ru.starkov.struct.db.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.starkov.struct.db.model.CustomerRequestDbModel;
import ru.starkov.dom.entity.CustomerRequest;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.dom.value.ResultData;

@Component
@RequiredArgsConstructor
public class CustomerRequestDbModelMapper {

    private final CustomerDbModelMapper customerDbModelMapper;
    private final RequestDataDbModelMapper requestDataDbModelMapper;


    public CustomerRequestDbModel toModel(CustomerRequest entity) {
        var customerRequestDbModel = new CustomerRequestDbModel();

        var id = entity.getCustomerRequestId().id();
        var customerDbModel = customerDbModelMapper.toModel(entity.getCustomer());
        var state = entity.getState().toString();
        var requestDataDbModel = requestDataDbModelMapper.toModel(entity.getRequestData());


        customerRequestDbModel.setId(id);
        customerRequestDbModel.setCustomerDbModel(customerDbModel);
        customerRequestDbModel.setState(state);
        customerRequestDbModel.setRequestDataDbModel(requestDataDbModel);

        var resultData = entity.getResultData();
        if (resultData != null) {
            customerRequestDbModel.setResultData(resultData.data());
        }
        var tokenCost = entity.getTokenCost();
        if (tokenCost != null) {
            customerRequestDbModel.setTokenCost(tokenCost);
        }

        return customerRequestDbModel;
    }

    public CustomerRequest toEntity(CustomerRequestDbModel model) {
        var customerRequestId = new CustomerRequestId(model.getId());
        var customer = customerDbModelMapper.toEntity(model.getCustomerDbModel());
        var state = CustomerRequest.State.valueOf(model.getState());
        var requestData = requestDataDbModelMapper.toEntity(model.getRequestDataDbModel());

        var customerRequest = CustomerRequest.builder()
                .customerRequestId(customerRequestId)
                .customer(customer)
                .state(state)
                .requestData(requestData)
                .build();

        var resultData = model.getResultData();
        if (resultData != null) {
            customerRequest = customerRequest.toBuilder()
                    .resultData(new ResultData(resultData))
                    .build();
        }
        var tokenCost = model.getTokenCost();
        if (tokenCost != null) {
            customerRequest = customerRequest.toBuilder()
                    .tokenCost(tokenCost)
                    .build();
        }

        return customerRequest;
    }
}