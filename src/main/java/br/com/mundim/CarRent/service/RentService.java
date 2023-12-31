package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.model.dto.RentDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.model.entity.Rent;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.repository.CarRepository;
import br.com.mundim.CarRent.repository.RentRepository;
import br.com.mundim.CarRent.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.*;
import static br.com.mundim.CarRent.model.entity.Car.Availability.AVAILABLE;
import static br.com.mundim.CarRent.model.entity.Car.Availability.UNAVAILABLE;
import static br.com.mundim.CarRent.model.entity.Rent.ReturnStatus.*;

@Service
public class RentService {

    private static final Double DELAY_FINE = 1.2;

    private final RentRepository rentRepository;
    private final CarService carService;
    private final CarRepository carRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final RentMailService rentMailService;

    @Autowired
    public RentService(
            RentRepository rentRepository,
            CarService carService,
            CarRepository carRepository,
            UserService userService,
            AuthenticationService authenticationService,
            @Lazy RentMailService rentMailService
    ) {
        this.rentRepository = rentRepository;
        this.carService = carService;
        this.carRepository = carRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.rentMailService = rentMailService;
    }

    public Rent create(RentDTO dto) {
        verifyUserCanDoAction(dto.userId());
        verifyAvailabilityCar(dto.carId());
        verifyRentDayIsBeforeReturnDay(dto.rentDay(), dto.returnDay());
        carService.setCarAvailability(dto.carId(), UNAVAILABLE);
        Rent rent = rentRepository.save(new Rent(dto));
        setRentTotalValue(rent);
        rentMailService.sucessfullRentMail(rent);
        return rent;
    }

    public Rent findById(Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(RENT_NOT_FOUND_BY_ID.params(id.toString()).getMessage()));
        verifyUserCanDoAction(rent.getUserId());
        return rent;
    }

    public List<Rent> findByUserId(Long userId) {
        verifyUserCanDoAction(userId);
        return rentRepository.findRentsByUserId(userId);
    }

    public List<Rent> findByCarId(Long carId) {
        carService.findById(carId);
        return rentRepository.findRentsByCarId(carId);
    }

    public Rent returnCar(Long rentId) {
        Rent rent = findById(rentId);
        verifyUserCanDoAction(rent.getUserId());
        verifyRentDayIsBeforeReturnDay(rent.getRentDay(), LocalDateTime.now());
        // Resolving car
        Car car = carService.findById(rent.getCarId());
        car.setAvailability(AVAILABLE);
        carRepository.save(car);
        // Resolving Total Value: In the actual scenario, the client pays the whole price, even if returned earlier, and pays 20% delay fine
        setRentReturnStatus(rent);
        setRentTotalValue(rent);
        rentMailService.sucessfullReturnMail(rent);
        return rentRepository.save(rent);
    }

    public Rent update(Long id, RentDTO dto) {
        verifyUserCanDoAction(dto.userId());
        verifyAvailabilityCar(dto.carId());
        Rent rent = findById(id);
        if (rent.getReturnStatus() != NOT_RETURNED)
            throw new BadRequestException(RENT_CANNOT_CHANGE_BECAUSE_CAR_ALREADY_RETURNED.getMessage());
        changeRentedCar(id, dto.carId());
        changeCustomerWhoRented(id, dto.userId());
        changeRentDay(id, dto.rentDay());
        changeReturnDay(id, dto.returnDay());
        return rentRepository.save(rent);
    }

    public Rent delete(Long id) {
        Rent rent = findById(id);
        verifyUserCanDoAction(rent.getUserId());
        if (rent.getReturnStatus().equals(NOT_RETURNED))
            returnCar(id);
        rentRepository.deleteById(rent.getId());
        return rent;
    }

    private void changeRentedCar(Long rentId, Long newCarId) {
        Rent rent = findById(rentId);
        carService.setCarAvailability(rent.getCarId(), AVAILABLE);
        rent.setCarId(newCarId);
        carService.setCarAvailability(newCarId, UNAVAILABLE);
        rentRepository.save(rent);
    }

    private void changeCustomerWhoRented(Long rentId, Long newCustomerId) {
        Rent rent = findById(rentId);
        verifyUserCanDoAction(newCustomerId);
        rent.setUserId(newCustomerId);
        rentRepository.save(rent);
    }

    private void changeRentDay(Long rentId, LocalDateTime newRentDate) {
        Rent rent = findById(rentId);
        rent.setRentDay(newRentDate);
        rentRepository.save(rent);
    }

    private void changeReturnDay(Long rentId, LocalDateTime newReturnDate) {
        Rent rent = findById(rentId);
        rent.setReturnDay(newReturnDate);
        rentRepository.save(rent);
    }

    private void setRentReturnStatus(Rent rent) {
        rent.setReturnStatus(RETURNED);
        LocalDate returnDay = rent.getReturnDay().toLocalDate();
        if (returnDay.isBefore(LocalDate.now())) {
            rent.setReturnStatus(LATE_RETURN);
            rent.setReturnDay(LocalDateTime.now());
        }
        if (returnDay.isAfter(LocalDate.now())) {
            rent.setReturnStatus(EARLY_RETURN);
            rent.setReturnDay(LocalDateTime.now());
        }
    }

    private void setRentTotalValue(Rent rent) {
        Double valuePerDay = carService.findById(rent.getCarId()).getRentalRatePerDay();

        long totalDays = calculateTotalRentDays(rent);
        if (rent.getReturnStatus().equals(LATE_RETURN))
            rent.setTotalValue(valuePerDay * totalDays * DELAY_FINE);
        else
            rent.setTotalValue(valuePerDay * totalDays);
    }

    private long calculateTotalRentDays(Rent rent) {
        long totalMinutes = Duration.between(rent.getRentDay(), rent.getReturnDay()).toMinutes();
        long totalDays = totalMinutes / 1440;
        if (totalMinutes % 1440 > 0) totalDays++;
        if (totalDays == 0) totalDays++;
        return totalDays;
    }

    private void verifyRentDayIsBeforeReturnDay(LocalDateTime rentDay, LocalDateTime returnDay) {
        if (rentDay.isAfter(returnDay))
            throw new BadRequestException(RETURN_DAY_BEFORE_RENT_DAY.getMessage());
    }

    private void verifyUserCanDoAction(Long customerId) {
        User user = userService.findById(customerId);
        authenticationService.verifyUserAuthentication(user);
    }

    private void verifyAvailabilityCar(Long carId) {
        Car car = carService.findById(carId);
        if (car.getAvailability() != AVAILABLE) {
            throw new BadRequestException(UNAVAILABLE_CAR.params(carId.toString()).getMessage());
        }
    }
}
