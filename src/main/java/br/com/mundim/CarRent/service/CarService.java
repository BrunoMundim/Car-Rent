package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.model.dto.CarDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.model.entity.Rent;
import br.com.mundim.CarRent.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.*;
import static br.com.mundim.CarRent.model.entity.Rent.ReturnStatus.NOT_RETURNED;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final RentService rentService;

    @Autowired
    public CarService(CarRepository carRepository, @Lazy RentService rentService) {
        this.carRepository = carRepository;
        this.rentService = rentService;
    }

    public Car create(CarDTO carDTO) {
        return carRepository.save(new Car(carDTO));
    }

    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(CAR_NOT_FOUND_BY_ID.params(id.toString()).getMessage()));
    }

    public Car findByPlate(String plate) {
        Car car = carRepository.findByPlate(plate);
        if(car == null)
            throw new BadRequestException(CAR_NOT_FOUND_BY_PLATE.params(plate).getMessage());
        return car;
    }

    public Car updateById(Long id, CarDTO dto) {
        Car car = findById(id);
        updateCar(car, dto);
        return carRepository.save(car);
    }

    public Car updateByPlate(String plate, CarDTO dto) {
        Car car = findByPlate(plate);
        updateCar(car, dto);
        return carRepository.save(car);
    }

    private void updateCar(Car car, CarDTO dto) {
        car.setPlate(dto.plate());
        car.setBrand(dto.brand());
        car.setModel(dto.model());
        car.setYear(dto.year());
        car.setColor(dto.color());
        car.setRentalRatePerDay(dto.rentalRatePerDay());
    }


    public Car deleteById(Long carId) {
        if(carIsRented(carId))
            throw new BadRequestException(CANNOT_DELETE_RENTED_CAR.params(carId.toString()).getMessage());
        Car car = findById(carId);
        carRepository.deleteById(car.getId());
        return car;
    }

    public Car deleteByPlate(String plate) {
        Car car = findByPlate(plate);
        if(carIsRented(car.getId()))
            throw new BadRequestException(CANNOT_DELETE_RENTED_CAR.params(car.getId().toString()).getMessage());
        carRepository.deleteById(car.getId());
        return car;
    }

    public void setCarAvailability(Long carId, Car.Availability availability) {
        Car car = findById(carId);
        car.setAvailability(availability);
        carRepository.save(car);
    }

    private boolean carIsRented(Long carId) {
        List<Rent> rentList = rentService.findByCarId(carId);
        for(Rent rent: rentList) {
            if(rent.getReturnStatus() == NOT_RETURNED)
                return true;
        }
        return false;
    }

}