package br.com.mundim.CarRent.controller;

import br.com.mundim.CarRent.model.dto.RentDTO;
import br.com.mundim.CarRent.model.entity.Rent;
import br.com.mundim.CarRent.service.RentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentController {

    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    @Operation(tags = "Rent", summary = "Create new Rent")
    public ResponseEntity<Rent> create(@RequestBody RentDTO dto) {
        return new ResponseEntity<>(rentService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("find-by-id")
    @Operation(tags = "Rent", summary = "Find Rent by Id")
    public ResponseEntity<Rent> findByRentId(@RequestParam Long id) {
        return ResponseEntity.ok(rentService.findById(id));
    }

    @GetMapping("find-by-customer-id")
    @Operation(tags = "Rent", summary = "Find Rents by Customer Id")
    public ResponseEntity<List<Rent>> findByCustomerId(@RequestParam Long customerId) {
        return ResponseEntity.ok(rentService.findByCustomerId(customerId));
    }

    @GetMapping("find-by-car-id")
    @Operation(tags = "Rent", summary = "Find Rents by Car Id")
    public ResponseEntity<List<Rent>> findByCarId(@RequestParam Long carId) {
        return ResponseEntity.ok(rentService.findByCarId(carId));
    }

    @PutMapping("/return-car")
    @Operation(tags = "Rent", summary = "Return Car by Rent ID")
    public ResponseEntity<Rent> returnCar(@RequestParam Long rentId) {
        return ResponseEntity.ok(rentService.returnCar(rentId));
    }

    @PutMapping
    @Operation(tags = "Rent", summary = "Update Rent by Id")
    public ResponseEntity<Rent> update(@RequestParam Long id, @RequestBody RentDTO dto) {
        return ResponseEntity.ok(rentService.update(id, dto));
    }

    @DeleteMapping
    @Operation(tags = "Rent", summary = "Delete Rent by Id")
    public ResponseEntity<Rent> delete(@RequestParam Long id) {
        return ResponseEntity.ok(rentService.delete(id));
    }

}
