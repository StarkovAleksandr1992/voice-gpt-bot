package ru.starkov.struct.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.starkov.struct.db.model.CustomerDbModel;
import ru.starkov.struct.db.repository.CustomerRepository;

import java.util.List;

/*created for testing*/

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;


    @GetMapping("/customers")
    public List<CustomerDbModel> getAllCustomers() {
        return customerRepository.findAllCustomers();
    }
}
