package br.com.mundim.CarRent.model.dto;

public record CarDTO(
        String brand,
        String model,
        Integer year,
        String color,
        Double rentalRatePerDay
) {
}
