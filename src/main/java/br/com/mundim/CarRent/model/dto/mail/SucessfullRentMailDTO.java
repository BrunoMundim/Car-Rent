package br.com.mundim.CarRent.model.dto.mail;

import java.time.LocalDateTime;

public record SucessfullRentMailDTO(
        String username,
        String brand,
        String model,
        String rentDay,
        String returnDay,
        String totalValue
) {
}
