package br.com.mundim.CarRent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CarDTO(
        @Schema(defaultValue = "BRA2E19") String plate,
        @Schema(defaultValue = "Chevrolet") String brand,
        @Schema(defaultValue = "Onix") String model,
        @Schema(defaultValue = "2018") Integer carYear,
        @Schema(defaultValue = "Cinza") String color,
        @Schema(defaultValue = "159.9") Double rentalRatePerDay
) {
}
