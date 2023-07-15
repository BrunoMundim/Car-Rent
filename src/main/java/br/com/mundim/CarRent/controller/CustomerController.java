package br.com.mundim.CarRent.controller;

import br.com.mundim.CarRent.model.dto.CustomerDTO;
import br.com.mundim.CarRent.model.entity.Customer;
import br.com.mundim.CarRent.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(tags = "Customer", summary = "Create a Customer")
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerDTO dto) {
        return new ResponseEntity<>(customerService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(tags = "Customer", summary = "Find a Customer by ID")
    public ResponseEntity<Customer> findById(@RequestParam Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/find-all")
    @Operation(tags = "Customer", summary = "Find all Customers")
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PutMapping
    @Operation(tags = "Customer", summary = "Update a Customer by ID")
    public ResponseEntity<Customer> update(@RequestParam Long id, @RequestBody CustomerDTO dto) {
        return ResponseEntity.ok(customerService.update(id, dto));
    }

    @DeleteMapping
    @Operation(tags = "Customer", summary = "Delete a Customer by ID")
    public ResponseEntity<Customer> deleteById(@RequestParam Long id) {
        return ResponseEntity.ok(customerService.deleteById(id));
    }

}
