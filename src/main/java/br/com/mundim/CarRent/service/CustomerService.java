package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.model.dto.CustomerDTO;
import br.com.mundim.CarRent.model.entity.Customer;
import br.com.mundim.CarRent.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.*;

@Service
public class CustomerService {

    private static final String emailExpectedFormat = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(CustomerDTO dto) {
        return customerRepository.save(new Customer(dto));
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(CUSTOMER_NOT_FOUND_BY_ID.params(id.toString()).getMessage()));
    }

    public Customer findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_EMAIL.params(email).getMessage());
        return customer;
    }

    public Customer findByCpf(String cpf) {
        Customer customer = customerRepository.findByCpf(cpf);
        if(customer == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_CPF.params(cpf).getMessage());
        return customer;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer update(Long id, CustomerDTO dto) {
        Customer customer = findById(id);
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setCpf(dto.cpf());
        customer.setContacts(dto.contacts());
        return customerRepository.save(customer);
    }

    public Customer deleteById(Long id) {
        Customer customer = findById(id);
        customerRepository.deleteById(id);
        return customer;
    }

}
