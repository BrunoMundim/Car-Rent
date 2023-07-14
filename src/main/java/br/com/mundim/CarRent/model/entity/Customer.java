package br.com.mundim.CarRent.model.entity;

import br.com.mundim.CarRent.model.dto.CustomerDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Field 'name' must not be empty.")
    private String name;
    @Email(message = "Field 'email' must be a well-formed email address.")
    private String email;
    @CPF(message = "Field 'cpf' must be a well-formed CPF.")
    private String cpf;
    @NotEmpty(message = "Field 'contacts' must have at least one contact.")
    private List<String> contacts;

    public Customer(CustomerDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.cpf = dto.cpf();
        this.contacts = dto.contacts();
    }

}
