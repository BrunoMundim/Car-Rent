package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.model.dto.CustomerDTO;
import br.com.mundim.CarRent.model.entity.Customer;
import br.com.mundim.CarRent.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(CustomerDTO dto) {
        return customerRepository.save(new Customer(dto));
    }

}
