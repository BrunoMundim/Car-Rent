package br.com.mundim.CarRent.controller;

import br.com.mundim.CarRent.model.dto.CarDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
@SecurityRequirement(name = "jwt")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    @Operation(tags = "Car", summary = "Create a Car")
    public ResponseEntity<Car> create(@RequestBody CarDTO dto) {
        return new ResponseEntity<>(carService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("find-by-id")
    @Operation(tags = "Car", summary = "Find Car by ID")
    public ResponseEntity<Car> findById(@RequestParam Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @GetMapping("find-by-plate")
    @Operation(tags = "Car", summary = "Find Car by Plate")
    public ResponseEntity<Car> findByPlate(@RequestParam String plate) {
        return ResponseEntity.ok(carService.findByPlate(plate));
    }

    @GetMapping("find-all")
    @Operation(tags = "Car", summary = "Find all Cars")
    public ResponseEntity<List<Car>> findAll() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("find-by-brand")
    @Operation(tags = "Car", summary = "Find Cars by Brand")
    public ResponseEntity<List<Car>> findByBrand(@RequestParam String brand) {
        return ResponseEntity.ok(carService.findByBrand(brand));
    }

    @GetMapping("find-by-model")
    @Operation(tags = "Car", summary = "Find Cars by Model")
    public ResponseEntity<List<Car>> findByModel(@RequestParam String model) {
        return ResponseEntity.ok(carService.findByModel(model));
    }

    @PutMapping("update-by-id")
    @Operation(tags = "Car", summary = "Update Car by ID")
    public ResponseEntity<Car> updateById(@RequestParam Long id, @RequestBody CarDTO dto) {
        return ResponseEntity.ok(carService.updateById(id, dto));
    }

    @PutMapping("update-by-plate")
    @Operation(tags = "Car", summary = "Update Car by Plate")
    public ResponseEntity<Car> updateByPlate(@RequestParam String plate, @RequestBody CarDTO dto) {
        return ResponseEntity.ok(carService.updateByPlate(plate, dto));
    }

    @DeleteMapping("delete-by-id")
    @Operation(tags = "Car", summary = "Delete Car by ID")
    public ResponseEntity<Car> deleteById(@RequestParam Long id) {
        return ResponseEntity.ok(carService.deleteById(id));
    }

    @DeleteMapping("delete-by-plate")
    @Operation(tags = "Car", summary = "Delete Car by Plate")
    public ResponseEntity<Car> deleteByPlate(@RequestParam String plate) {
        return ResponseEntity.ok(carService.deleteByPlate(plate));
    }

}
