package br.com.mundim.CarRent.model.entity;

import br.com.mundim.CarRent.model.dto.RentDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.mundim.CarRent.model.entity.Rent.ReturnStatus.NOT_RETURNED;

@Entity
@Table(name = "rent")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rent {

    public enum ReturnStatus {
        NOT_RETURNED,
        RETURNED,
        LATE_RETURN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Min(value = 1)
    private Long customerId;
    @NotNull
    @Min(value = 1)
    private Long carId;
    @NotNull
    private LocalDateTime rentDay;
    @NotNull
    private LocalDateTime returnDay;
    private Double totalValue;
    private ReturnStatus returnStatus;

    public Rent(RentDTO dto) {
        this.customerId = dto.customerId();
        this.carId = dto.carId();
        this.rentDay = dto.rentDay();
        this.returnDay = dto.returnDay();
        this.totalValue = 0.0;
        this.returnStatus = NOT_RETURNED;
    }

}
