package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.dom.entity.TokenAccount;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.app.usecase.WithdrawTokens;
import ru.starkov.dom.value.CustomerSettings;

import java.math.BigInteger;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WithdrawTokensImpl implements WithdrawTokens {

    private final CustomerRequestGateWay customerRequestGateWay;
    private final Map<CustomerSettings.GptModel, BigInteger> gptModelRate = Map.of(
            CustomerSettings.GptModel.YANDEX_GPT, BigInteger.valueOf(5),
            CustomerSettings.GptModel.YANDEX_GPT_LITE, BigInteger.ONE
    );

    @Override
    public void withdrawTokens(CustomerRequestId id) {

        var customerRequest = customerRequestGateWay.findById(id)
                .orElseThrow(() -> new RuntimeException("No customer request found with given ID: " + id.toString()));

        var customerSettings = customerRequest.getCustomer().getCustomerSettings();
        if (customerSettings.getDefaultActionType() == CustomerSettings.ActionType.ASK_GPT ||
                customerSettings.getDefaultActionType() == CustomerSettings.ActionType.ASK_GPT_WITH_PROMPT) {
            var gptModel = customerSettings.getDefaultGptModel();

            TokenAccount tokenAccount = customerRequest.getCustomer().getTokenAccount();
            var tokenCost = customerRequest.getTokenCost();

            tokenAccount.withdraw(tokenCost.multiply(gptModelRate.get(gptModel)));
            customerRequestGateWay.save(customerRequest);
        }
    }
}
