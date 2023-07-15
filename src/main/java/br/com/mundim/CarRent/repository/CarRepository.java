package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    public Car findByPlate(String plate);

}
