package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.model.dto.CarDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.model.entity.Rent;
import br.com.mundim.CarRent.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.*;
import static br.com.mundim.CarRent.model.entity.Car.Availability.AVAILABLE;
import static br.com.mundim.CarRent.model.entity.Rent.ReturnStatus.NOT_RETURNED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    private static CarDTO carDTO;
    private static CarDTO updateCarDTO;
    private static Car car;
    private static Car car2;

    @Mock
    private CarRepository carRepository;

    @Mock
    private RentService rentService;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setup() {
        car = Car.builder()
                .id(1L).plate("BRA2E19").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(AVAILABLE).rentalRatePerDay(159.9)
                .build();
        car2 = Car.builder()
                .id(2L).plate("BRA2E12").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").availability(AVAILABLE).rentalRatePerDay(159.9)
                .build();
        carDTO = CarDTO.builder()
                .plate("BRA2E19").brand("Chevrolet").model("Onix")
                .carYear(2018).color("Cinza").rentalRatePerDay(159.9)
                .build();
        updateCarDTO = CarDTO.builder()
                .plate("BRA2E10").brand("Ford").model("Fiesta")
                .carYear(220).color("Branco").rentalRatePerDay(119.9)
                .build();
    }

    @Test
    public void create_shouldReturnCreatedCar() {
        when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Car createdCar = carService.create(carDTO);

        assertThat(createdCar).isNotNull();
        assertThat(createdCar.getPlate()).isEqualTo(carDTO.plate());
        assertThat(createdCar.getBrand()).isEqualTo(carDTO.brand());
        assertThat(createdCar.getModel()).isEqualTo(carDTO.model());
        assertThat(createdCar.getCarYear()).isEqualTo(carDTO.carYear());
        assertThat(createdCar.getColor()).isEqualTo(carDTO.color());
        assertThat(createdCar.getRentalRatePerDay()).isEqualTo(carDTO.rentalRatePerDay());
    }

    @Test
    public void findById_shouldReturnFoundCar() {
        when(carRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(car));

        Car foundCar = carService.findById(car.getId());

        assertThat(foundCar).isNotNull();
        assertThat(foundCar).isEqualTo(car);
    }

    @Test
    public void findById_shouldThrowCarNotFoundById() {
        when(carRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> carService.findById(car.getId()));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(CAR_NOT_FOUND_BY_ID.params(car.getId().toString()).getMessage());
    }

    @Test
    public void findByPlate_shouldReturnFoundCar() {
        when(carRepository.findByPlate(Mockito.any(String.class))).thenReturn(car);

        Car foundCar = carService.findByPlate(car.getPlate());

        assertThat(foundCar).isNotNull();
        assertThat(foundCar).isEqualTo(car);
    }

    @Test
    public void findByPlate_shouldThrowCarNotFoundByPlate() {
        when(carRepository.findByPlate(Mockito.any(String.class))).thenReturn(null);

        Throwable throwable = catchThrowable(() -> carService.findByPlate(car.getPlate()));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(CAR_NOT_FOUND_BY_PLATE.params(car.getPlate()).getMessage());
    }

    @Test
    public void findByBrand_shouldReturnTwoFoundCars() {
        List<Car> cars = List.of(car, car2);
        when(carRepository.findByBrand(Mockito.any(String.class))).thenReturn(cars);

        List<Car> foundCars = carService.findByBrand(car.getBrand());

        assertThat(foundCars).isNotNull();
        assertThat(foundCars.size()).isEqualTo(2);
    }

    @Test
    public void findByBrand_shouldThrowNoCarLocatedBrand() {
        when(carRepository.findByBrand(Mockito.any(String.class))).thenReturn(List.of());

        Throwable throwable = catchThrowable(() -> carService.findByBrand(car.getBrand()));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(NO_CAR_LOCATED_BRAND.params(car.getBrand()).getMessage());
    }

    @Test
    public void findByModel_shouldReturnTwoFoundCars() {
        List<Car> cars = List.of(car, car2);
        when(carRepository.findByModel(Mockito.any(String.class))).thenReturn(cars);

        List<Car> foundCars = carService.findByModel(car.getModel());

        assertThat(foundCars).isNotNull();
        assertThat(foundCars.size()).isEqualTo(2);
    }

    @Test
    public void findByModel_shouldThrowNoCarLocatedModel() {
        when(carRepository.findByModel(Mockito.any(String.class))).thenReturn(List.of());

        Throwable throwable = catchThrowable(() -> carService.findByModel(car.getModel()));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(NO_CAR_LOCATED_MODEL.params(car.getModel()).getMessage());
    }

    @Test
    public void findAll_shouldReturnTwoFoundCars() {
        List<Car> cars = List.of(car, car2);
        when(carRepository.findAll()).thenReturn(cars);

        List<Car> foundCars = carService.findAll();

        assertThat(foundCars).isNotNull();
        assertThat(foundCars.size()).isEqualTo(2);
    }

    @Test
    public void updateById_shouldReturnUpdatedCar() {
        when(carRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(car));
        when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Car updatedCar = carService.updateById(car.getId(), updateCarDTO);

        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar.getPlate()).isEqualTo(updateCarDTO.plate());
        assertThat(updatedCar.getBrand()).isEqualTo(updateCarDTO.brand());
        assertThat(updatedCar.getModel()).isEqualTo(updateCarDTO.model());
        assertThat(updatedCar.getCarYear()).isEqualTo(updateCarDTO.carYear());
        assertThat(updatedCar.getColor()).isEqualTo(updateCarDTO.color());
        assertThat(updatedCar.getRentalRatePerDay()).isEqualTo(updateCarDTO.rentalRatePerDay());
    }

    @Test
    public void updateByPlate_shouldReturnUpdatedCar() {
        when(carRepository.findByPlate(Mockito.any(String.class))).thenReturn(car);
        when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Car updatedCar = carService.updateByPlate(car.getPlate(), updateCarDTO);

        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar.getPlate()).isEqualTo(updateCarDTO.plate());
        assertThat(updatedCar.getBrand()).isEqualTo(updateCarDTO.brand());
        assertThat(updatedCar.getModel()).isEqualTo(updateCarDTO.model());
        assertThat(updatedCar.getCarYear()).isEqualTo(updateCarDTO.carYear());
        assertThat(updatedCar.getColor()).isEqualTo(updateCarDTO.color());
        assertThat(updatedCar.getRentalRatePerDay()).isEqualTo(updateCarDTO.rentalRatePerDay());
    }

    @Test
    public void deleteById_shouldReturnDeletedCar() {
        when(rentService.findByCarId(Mockito.any(Long.class))).thenReturn(List.of());
        when(carRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(car));

        Car deletedCar = carService.deleteById(car.getId());

        assertThat(deletedCar).isEqualTo(car);
    }

    @Test
    public void deleteById_shouldThrowCannotDeleteRentedCar() {
        Rent rent = Rent.builder()
                .carId(car.getId()).returnStatus(NOT_RETURNED)
                .build();
        when(rentService.findByCarId(Mockito.any(Long.class))).thenReturn(List.of(rent));

        Throwable throwable = catchThrowable(() -> carService.deleteById(car.getId()));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(CANNOT_DELETE_RENTED_CAR.params(car.getId().toString()).getMessage());
    }

    @Test
    public void deleteByPlate_shouldReturnDeletedCar() {
        when(rentService.findByCarId(Mockito.any(Long.class))).thenReturn(List.of());
        when(carRepository.findByPlate(Mockito.any(String.class))).thenReturn(car);
        when(carRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(car));

        Car deletedCar = carService.deleteByPlate(car.getPlate());

        assertThat(deletedCar).isEqualTo(car);
    }

}
