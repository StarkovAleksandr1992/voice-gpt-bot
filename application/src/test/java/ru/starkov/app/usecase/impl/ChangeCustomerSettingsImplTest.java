package ru.starkov.app.usecase.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.starkov.app.dto.in.CustomerSettingsActionTypeInfo;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.TokenAccount;
import ru.starkov.dom.entity.identifier.CustomerId;
import ru.starkov.dom.entity.identifier.TokenAccountId;
import ru.starkov.dom.value.CustomerSettings;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ChangeCustomerSettingsImplTest {
}