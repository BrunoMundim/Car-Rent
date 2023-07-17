package br.com.mundim.CarRent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentDTO(
        @Schema(defaultValue = "1") Long userId,
        @Schema(defaultValue = "1") Long carId,
        LocalDateTime rentDay,
        LocalDateTime returnDay
) {
}
