package br.com.mundim.CarRent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RentDTO(
        @Schema(defaultValue = "1") Long customerId,
        @Schema(defaultValue = "1") Long carId,
        LocalDateTime rentDay,
        LocalDateTime returnDay
) {
}
