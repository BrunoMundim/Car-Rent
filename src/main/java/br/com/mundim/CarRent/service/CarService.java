package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.model.dto.CarDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.CAR_NOT_FOUND_BY_ID;
import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.CAR_NOT_FOUND_BY_PLATE;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
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


    public Car deleteById(Long id) {
        Car car = findById(id);
        carRepository.deleteById(car.getId());
        return car;
    }

    public Car deleteByPlate(String plate) {
        Car car = findByPlate(plate);
        carRepository.deleteById(car.getId());
        return car;
    }

}
