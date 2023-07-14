package br.com.mundim.CarRent.model.dto;

import java.util.List;

public record CustomerDTO(
        String name,
        String cpf,
        List<String> contacts
) {
}
