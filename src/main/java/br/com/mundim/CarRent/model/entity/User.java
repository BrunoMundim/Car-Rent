package br.com.mundim.CarRent.model.entity;

import br.com.mundim.CarRent.model.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Field 'name' must not be empty.")
    private String name;
    @Column(unique = true)
    @Email(message = "Field 'email' must be a well-formed email address.")
    private String email;
    @CPF(message = "Field 'cpf' must be a well-formed CPF.")
    private String cpf;
    @NotEmpty(message = "Field 'password' must not be empty.")
    private String password;
    @NotEmpty(message = "Field 'contacts' must have at least one contact.")
    private List<String> contacts;
    private String role;

    public User(UserDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.cpf = dto.cpf();
        this.contacts = dto.contacts();
        this.role = "ROLE_USER";
        this.password = passwordEncoder().encode(dto.password());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    public String getUsername() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

}
