package br.com.mundim.CarRent.model.dto.mail;

public record SucessfullReturnMailDTO(
        String username,
        String brand,
        String model,
        String rentDay,
        String returnDay,
        String totalValue,
        String returnStatus
) {
}
