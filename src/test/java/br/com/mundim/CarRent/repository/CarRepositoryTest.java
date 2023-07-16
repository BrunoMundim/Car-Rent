package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.dto.CarDTO;
import br.com.mundim.CarRent.model.entity.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static br.com.mundim.CarRent.model.entity.Car.Availability.AVAILABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    private static Car car;
    private static Car car2;

    @BeforeEach
    public void setup() {
        car = Car.builder()
                .plate("BRA2E19").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(AVAILABLE).rentalRatePerDay(159.9)
                .build();
        car2 = Car.builder()
                .plate("BRA2E12").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(AVAILABLE).rentalRatePerDay(159.9)
                .build();
    }


    @Test
    public void save_shouldReturnSavedCar() {
        Car savedCar = carRepository.save(car);

        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_shouldReturnTwoCars() {
        carRepository.save(car);
        carRepository.save(car2);

        List<Car> carList = carRepository.findAll();

        assertThat(carList).isNotNull();
        assertThat(carList.size()).isEqualTo(2);
    }

    @Test
    public void findByBrand_shouldReturnTwoCars() {
        carRepository.save(car);
        carRepository.save(car2);

        List<Car> carList = carRepository.findByBrand(car.getBrand());

        assertThat(carList).isNotNull();
        assertThat(carList.size()).isEqualTo(2);
    }

    @Test
    public void findByModel_shouldReturnTwoCars() {
        carRepository.save(car);
        carRepository.save(car2);

        List<Car> carList = carRepository.findByModel(car.getModel());

        assertThat(carList).isNotNull();
        assertThat(carList.size()).isEqualTo(2);
    }

    @Test
    public void findById_shouldReturnFoundCar() {
        carRepository.save(car);

        Car foundCar = carRepository.findById(car.getId()).orElse(null);

        assertThat(foundCar).isNotNull();
    }

    @Test
    public void findByPlate_shouldReturnFoundCar() {
        carRepository.save(car);

        Car foundCar = carRepository.findByPlate(car.getPlate());

        assertThat(foundCar).isNotNull();
    }

    @Test
    public void deleteById_shouldDeleteCar() {
        carRepository.save(car);

        carRepository.deleteById(car.getId());
        Car foundCar = carRepository.findById(car.getId()).orElse(null);

        assertThat(foundCar).isNull();
    }

}
