package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.model.dto.UserDTO;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.*;

@Service
public class UserService {

    private static final String emailExpectedFormat = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(UserDTO dto) {
        return userRepository.save(new User(dto));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(CUSTOMER_NOT_FOUND_BY_ID.params(id.toString()).getMessage()));
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_EMAIL.params(email).getMessage());
        return user;
    }

    public User findByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf);
        if(user == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_CPF.params(cpf).getMessage());
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, UserDTO dto) {
        User user = findById(id);
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setCpf(dto.cpf());
        user.setContacts(dto.contacts());
        user.setPassword(dto.password());
        return userRepository.save(user);
    }

    public User deleteById(Long id) {
        User user = findById(id);
        userRepository.deleteById(id);
        return user;
    }

}
