package ru.starkov.app.usecase;

import ru.starkov.app.dto.in.RequestDataInfo;

public interface CreateCustomerRequest {
    void execute(RequestDataInfo<?> data);
}
