package ru.starkov.struct.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.starkov.struct.db.model.CustomerDbModel;
import ru.starkov.struct.db.model.CustomerRequestDbModel;
import ru.starkov.struct.db.repository.CustomerRequestRepository;

import java.util.List;

/*created for testing*/

@RestController
@RequiredArgsConstructor
public class CustomerRequestWebController {

    private final CustomerRequestRepository customerRequestRepository;


    @GetMapping("/customer-requests")
    public List<CustomerRequestDbModel> customerRequestDbModels() {
        return customerRequestRepository.findAll();
    }
    @GetMapping("/customer-requests/customers")
    public List<CustomerDbModel> customerDbModels(){
        return customerRequestRepository.findAll().stream().map(CustomerRequestDbModel::getCustomerDbModel).toList();
    }
}
