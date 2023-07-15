package br.com.mundim.CarRent.model.entity;

import br.com.mundim.CarRent.model.dto.CarDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static br.com.mundim.CarRent.model.entity.Car.Availability.AVAILABLE;

@Entity
@Table(name = "car")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    public enum Availability {
        AVAILABLE,
        UNAVAILABLE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Plate must not be empty")
    @Size(min = 7, max = 7, message = "Plate must have 7 caracters")
    @Column(unique = true)
    private String plate;
    @NotEmpty(message = "Brand must not be empty")
    private String brand;
    @NotEmpty(message = "Model must not be empty")
    private String model;
    @NotNull(message = "Year must not be null")
    @Min(value = 1, message = "Year must be greater than 0.")
    private Integer year;
    @NotEmpty(message = "Color must not be empty")
    private String color;
    private Availability availability;
    @NotNull(message = "Rental Rate Per Day must not be null.")
    @Min(value = 1, message = "Rental Rate Per Day must be greater than 0.")
    private Double rentalRatePerDay;

    public Car(CarDTO dto){
        this.plate = dto.plate();
        this.brand = dto.brand();
        this.model = dto.model();
        this.year = dto.year();
        this.color = dto.color();
        this.rentalRatePerDay = dto.rentalRatePerDay();
        this.availability = AVAILABLE;
    }

}
