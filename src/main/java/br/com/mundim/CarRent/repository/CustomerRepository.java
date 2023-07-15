package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    Customer findByCpf(String cpf);

}
