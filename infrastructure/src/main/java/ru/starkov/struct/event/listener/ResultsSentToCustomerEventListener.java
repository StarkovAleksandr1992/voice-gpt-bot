package ru.starkov.struct.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.starkov.dom.event.ResultsSentToCustomerEvent;
import ru.starkov.app.usecase.WithdrawTokens;

@Component
@RequiredArgsConstructor
public class ResultsSentToCustomerEventListener {

    private final WithdrawTokens withdrawTokens;

    @EventListener
    public void onResultsSentToCustomer(ResultsSentToCustomerEvent event) {
        withdrawTokens.withdrawTokens(event.id());
    }
}
