package br.com.mundim.CarRent.controller;

import br.com.mundim.CarRent.model.dto.CarDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    @Operation(tags = "Car", summary = "Create a Car")
    public Car create(@RequestBody CarDTO dto) {
        return carService.create(dto);
    }

    @GetMapping("find-by-id")
    @Operation(tags = "Car", summary = "Find Car by ID")
    public Car findById(@RequestParam Long id) {
        return carService.findById(id);
    }

    @GetMapping("find-by-plate")
    @Operation(tags = "Car", summary = "Find Car by Plate")
    public Car findByPlate(@RequestParam String plate) {
        return carService.findByPlate(plate);
    }

    @PutMapping("update-by-id")
    @Operation(tags = "Car", summary = "Update Car by ID")
    public Car updateById(@RequestParam Long id, @RequestBody CarDTO dto) {
        return carService.updateById(id, dto);
    }

    @PutMapping("update-by-plate")
    @Operation(tags = "Car", summary = "Update Car by Plate")
    public Car updateByPlate(@RequestParam String plate, @RequestBody CarDTO dto) {
        return carService.updateByPlate(plate, dto);
    }

    @DeleteMapping("delete-by-id")
    @Operation(tags = "Car", summary = "Delete Car by ID")
    public Car deleteById(@RequestParam Long id) {
        return carService.deleteById(id);
    }

    @DeleteMapping("delete-by-plate")
    @Operation(tags = "Car", summary = "Delete Car by Plate")
    public Car deleteByPlate(@RequestParam String plate) {
        return carService.deleteByPlate(plate);
    }

}
