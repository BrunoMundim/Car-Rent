package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.exception.config.BaseErrorMessage;
import br.com.mundim.CarRent.model.dto.RentDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.model.entity.Rent;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.repository.CarRepository;
import br.com.mundim.CarRent.repository.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.*;
import static br.com.mundim.CarRent.model.entity.Car.Availability.AVAILABLE;
import static br.com.mundim.CarRent.model.entity.Car.Availability.UNAVAILABLE;
import static br.com.mundim.CarRent.model.entity.Rent.ReturnStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RentServiceTest {

    private static RentDTO rentDTO;
    private static Rent rent;
    private static Rent rent2;
    private static Car car;
    private static User user;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private UserService userService;

    @Mock
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private RentService rentService;

    @BeforeEach
    public void setup() {
        rent = Rent.builder()
                .id(1L).userId(1L).carId(1L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2)).returnStatus(NOT_RETURNED)
                .totalValue(0.0)
                .build();
        rent2 = Rent.builder()
                .id(2L).userId(1L).carId(1L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2)).returnStatus(NOT_RETURNED)
                .totalValue(0.0)
                .build();
        rentDTO = RentDTO.builder()
                .userId(1L).carId(1L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2))
                .build();
        car = Car.builder()
                .id(1L).plate("BRA2E19").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(AVAILABLE).rentalRatePerDay(159.9)
                .build();
        user = User.builder()
                .id(1L).name("Bruno Mundim").email("bruno@email.com").cpf("653.189.520-97")
                .password("password").contacts(List.of("32323232"))
                .role("ROLE_USER")
                .build();
    }

    @Test
    public void create_shouldReturnCreatedRent() {
        when(rentRepository.save(Mockito.any(Rent.class))).thenReturn(rent);
        when(userService.findById(Mockito.any(Long.class))).thenReturn(user);
        when(carService.findById(Mockito.any(Long.class))).thenReturn(car);

        Rent createdRent = rentService.create(rentDTO);

        assertThat(createdRent).isNotNull();
        assertThat(createdRent.getUserId()).isEqualTo(rentDTO.userId());
        assertThat(createdRent.getCarId()).isEqualTo(rentDTO.carId());
        assertThat(createdRent.getRentDay().toLocalDate()).isEqualTo(rentDTO.rentDay().toLocalDate());
        assertThat(createdRent.getReturnDay().toLocalDate()).isEqualTo(rentDTO.returnDay().toLocalDate());
        assertThat(createdRent.getReturnStatus()).isEqualTo(NOT_RETURNED);
        assertThat(createdRent.getTotalValue()).isEqualTo(0.0);
    }

    @Test
    public void create_shouldThrowUnavailableCar() {
        car.setAvailability(UNAVAILABLE);
        when(carService.findById(Mockito.any(Long.class))).thenReturn(car);

        Throwable throwable = catchThrowable(() -> rentService.create(rentDTO));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(UNAVAILABLE_CAR.params(car.getId().toString()).getMessage());
    }

    @Test
    public void findById_shouldReturnFoundRent() {
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(rent));

        Rent foundRent = rentService.findById(rent.getId());

        assertThat(foundRent).isEqualTo(rent);
    }

    @Test
    public void findById_shouldThrowRentNotFoundById() {
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> rentService.findById(rent.getId()));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(RENT_NOT_FOUND_BY_ID.params(rent.getId().toString()).getMessage());
    }

    @Test
    public void findByCustomerId_shouldReturnTwoFoundRent() {
        List<Rent> rents = List.of(rent, rent2);
        when(rentRepository.findRentsByCustomerId(Mockito.any(Long.class))).thenReturn(rents);

        List<Rent> foundRent = rentService.findByCustomerId(rent.getUserId());

        assertThat(foundRent).isEqualTo(rents);
        assertThat(foundRent.size()).isEqualTo(2);
    }

    @Test
    public void findByCarId_shouldReturnTwoFoundRent() {
        List<Rent> rents = List.of(rent, rent2);
        when(rentRepository.findRentsByCarId(Mockito.any(Long.class))).thenReturn(rents);

        List<Rent> foundRent = rentService.findByCarId(rent.getCarId());

        assertThat(foundRent).isEqualTo(rents);
        assertThat(foundRent.size()).isEqualTo(2);
    }

    @Test
    public void returnCar_shouldReturnReturnedRent() {
        rent = Rent.builder()
                .id(1L).userId(1L).carId(1L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusHours(2)).returnStatus(NOT_RETURNED)
                .totalValue(0.0)
                .build();
        car.setAvailability(UNAVAILABLE); // Setting car as rented
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(rent));
        when(rentRepository.save(Mockito.any(Rent.class))).thenReturn(rent);
        when(carService.findById(Mockito.any(Long.class))).thenReturn(car);
        when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Rent returnedRent = rentService.returnCar(rent.getId());

        assertThat(returnedRent).isNotNull();
        assertThat(returnedRent.getReturnStatus()).isEqualTo(RETURNED);
        assertThat(returnedRent.getTotalValue()).isEqualTo(car.getRentalRatePerDay());
    }

    @Test
    public void returnCar_shouldReturnEarlyReturnedRent() {
        rent = Rent.builder()
                .id(1L).userId(1L).carId(1L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(1)).returnStatus(NOT_RETURNED)
                .totalValue(0.0)
                .build();
        car.setAvailability(UNAVAILABLE); // Setting car as rented
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(rent));
        when(rentRepository.save(Mockito.any(Rent.class))).thenReturn(rent);
        when(carService.findById(Mockito.any(Long.class))).thenReturn(car);
        when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Rent returnedRent = rentService.returnCar(rent.getId());

        assertThat(returnedRent).isNotNull();
        assertThat(returnedRent.getReturnStatus()).isEqualTo(EARLY_RETURN);
        assertThat(returnedRent.getTotalValue()).isEqualTo(car.getRentalRatePerDay());
    }

    @Test
    public void returnCar_shouldReturnLateReturnedRent() {
        rent = Rent.builder()
                .id(1L).userId(1L).carId(1L).rentDay(LocalDateTime.now().minusDays(2))
                .returnDay(LocalDateTime.now().minusDays(1)).returnStatus(NOT_RETURNED)
                .totalValue(0.0)
                .build();
        car.setAvailability(UNAVAILABLE); // Setting car as rented
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(rent));
        when(rentRepository.save(Mockito.any(Rent.class))).thenReturn(rent);
        when(carService.findById(Mockito.any(Long.class))).thenReturn(car);
        when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Rent returnedRent = rentService.returnCar(rent.getId());

        assertThat(returnedRent).isNotNull();
        assertThat(returnedRent.getReturnStatus()).isEqualTo(LATE_RETURN);
        assertThat(returnedRent.getTotalValue()).isEqualTo(383.76);
    }

    @Test
    public void updateRent_shouldReturnUpdatedRent() {
        Car newCar = Car.builder()
                .id(2L).plate("BRA2E19").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(AVAILABLE).rentalRatePerDay(159.9)
                .build();
        User newUser = User.builder()
                .id(2L).name("Bruno Mundim").email("bruno@email.com").cpf("653.189.520-97")
                .password("password").contacts(List.of("32323232"))
                .role("ROLE_USER")
                .build();
        RentDTO updateRentDTO = RentDTO.builder()
                .userId(2L).carId(2L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2))
                .build();
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(rent));
        when(rentRepository.save(Mockito.any(Rent.class))).thenReturn(rent);
        when(carService.findById(Mockito.any(Long.class))).thenReturn(newCar);
        when(userService.findById(Mockito.any(Long.class))).thenReturn(newUser);

        Rent updatedRent = rentService.update(rent.getId(), updateRentDTO);

        assertThat(updatedRent.getUserId()).isEqualTo(updateRentDTO.userId());
        assertThat(updatedRent.getCarId()).isEqualTo(updateRentDTO.carId());
        assertThat(updatedRent.getRentDay()).isEqualTo(updateRentDTO.rentDay());
        assertThat(updatedRent.getReturnDay()).isEqualTo(updateRentDTO.returnDay());
    }

    @Test
    public void updateRent_shouldThrowUnavailableCar() {
        Car newCar = Car.builder()
                .id(2L).plate("BRA2E19").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(UNAVAILABLE).rentalRatePerDay(159.9)
                .build();
        RentDTO updateRentDTO = RentDTO.builder()
                .userId(2L).carId(2L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2))
                .build();
        when(carService.findById(Mockito.any(Long.class))).thenReturn(newCar);

        Throwable throwable = catchThrowable(() -> rentService.update(rent.getId(), updateRentDTO));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(UNAVAILABLE_CAR.params(newCar.getId().toString()).getMessage());
    }

    @Test
    public void updateRent_shouldThrowRentCannotChangeBecauseCarAlreadyReturned() {
        rent.setReturnStatus(RETURNED);
        Car newCar = Car.builder()
                .id(2L).plate("BRA2E19").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(AVAILABLE).rentalRatePerDay(159.9)
                .build();
        User newUser = User.builder()
                .id(2L).name("Bruno Mundim").email("bruno@email.com").cpf("653.189.520-97")
                .password("password").contacts(List.of("32323232"))
                .role("ROLE_USER")
                .build();
        RentDTO updateRentDTO = RentDTO.builder()
                .userId(2L).carId(2L).rentDay(LocalDateTime.now())
                .returnDay(LocalDateTime.now().plusDays(2))
                .build();
        when(carService.findById(Mockito.any(Long.class))).thenReturn(newCar);
        when(userService.findById(Mockito.any(Long.class))).thenReturn(newUser);
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(rent));

        Throwable throwable = catchThrowable(() -> rentService.update(rent.getId(), updateRentDTO));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(RENT_CANNOT_CHANGE_BECAUSE_CAR_ALREADY_RETURNED.getMessage());
    }

    @Test
    public void delete_shouldReturnDeletedRent() {
        rent = Rent.builder()
                .id(1L).userId(1L).carId(1L).rentDay(LocalDateTime.now().minusDays(2))
                .returnDay(LocalDateTime.now().minusDays(1)).returnStatus(NOT_RETURNED)
                .totalValue(0.0)
                .build();
        car.setAvailability(UNAVAILABLE); // Setting car as rented
        when(rentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(rent));
        when(rentRepository.save(Mockito.any(Rent.class))).thenReturn(rent);
        when(carService.findById(Mockito.any(Long.class))).thenReturn(car);
        when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Rent deletedRent = rentService.delete(rent.getId());

        assertThat(deletedRent).isEqualTo(rent);
    }

}
