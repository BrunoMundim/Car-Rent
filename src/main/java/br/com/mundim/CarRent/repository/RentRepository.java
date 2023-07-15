package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    public List<Rent> findRentsByCustomerId(Long customerId);

    public List<Rent> findRentsByCarId(Long carId);

}
