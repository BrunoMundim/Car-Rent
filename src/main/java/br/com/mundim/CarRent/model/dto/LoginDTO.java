package br.com.mundim.CarRent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDTO(
        @Schema(defaultValue = "brunomundim@email.com") String email,
        @Schema(defaultValue = "password") String password
) {
}
