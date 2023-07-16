package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.entity.Rent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RentRepositoryTest {

    private static Rent rent;
    private static Rent rent2;

    @Autowired
    private RentRepository rentRepository;

    @BeforeEach
    public void setup() {
        rent = Rent.builder()
                .customerId(1L).carId(1L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2))
                .build();
        rent2 = Rent.builder()
                .customerId(1L).carId(1L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    public void createRent_shouldReturnCreatedRent() {
        Rent savedRent = rentRepository.save(rent);

        assertThat(savedRent).isNotNull();
        assertThat(savedRent.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_shouldReturnTwoRents() {
        rentRepository.save(rent);
        rentRepository.save(rent2);

        List<Rent> rentList = rentRepository.findAll();

        assertThat(rentList).isNotNull();
        assertThat(rentList.size()).isEqualTo(2);
    }

    @Test
    public void findByCustomerId_shouldReturnTwoRents() {
        rentRepository.save(rent);
        rentRepository.save(rent2);

        List<Rent> rentList = rentRepository.findRentsByCustomerId(rent.getCustomerId());

        assertThat(rentList).isNotNull();
        assertThat(rentList.size()).isEqualTo(2);
    }

    @Test
    public void findByCarId_shouldReturnTwoRents() {
        rentRepository.save(rent);
        rentRepository.save(rent2);

        List<Rent> rentList = rentRepository.findRentsByCustomerId(rent.getCarId());

        assertThat(rentList).isNotNull();
        assertThat(rentList.size()).isEqualTo(2);
    }

    @Test
    public void findById_shouldReturnFoundRent() {
        rentRepository.save(rent);

        Rent foundRent = rentRepository.findById(rent.getId()).orElse(null);

        assertThat(foundRent).isNotNull();
    }

    @Test
    public void deleteById_shouldDeleteRent() {
        rentRepository.save(rent);

        rentRepository.deleteById(rent.getId());
        Rent foundRent = rentRepository.findById(rent.getId()).orElse(null);

        assertThat(foundRent).isNull();
    }

}
