package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {
}
