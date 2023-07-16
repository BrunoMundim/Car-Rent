package br.com.mundim.CarRent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UserDTO(
        @Schema(defaultValue = "Bruno Mundim") String name,
        @Schema(defaultValue = "brunomundim@email.com") String email,
        @Schema(defaultValue = "password") String password,
        @Schema(defaultValue = "974.585.030-68") String cpf,
        @Schema(defaultValue = "[ 34991970167 ]") List<String> contacts
) {
}
