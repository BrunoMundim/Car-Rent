package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.model.dto.UserDTO;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.repository.UserRepository;
import br.com.mundim.CarRent.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.*;

@Service
public class UserService {

    private static final String emailExpectedFormat = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public User create(UserDTO dto) {
        return userRepository.save(new User(dto));
    }

    public User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(CUSTOMER_NOT_FOUND_BY_ID.params(id.toString()).getMessage()));
        authenticationService.verifyUserAuthentication(user);
        return user;
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        authenticationService.verifyUserAuthentication(user);
        if(user == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_EMAIL.params(email).getMessage());
        return user;
    }

    public User findByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf);
        authenticationService.verifyUserAuthentication(user);
        if(user == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_CPF.params(cpf).getMessage());
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, UserDTO dto) {
        User user = findById(id);
        authenticationService.verifyUserAuthentication(user);
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setCpf(dto.cpf());
        user.setContacts(dto.contacts());
        user.setPassword(dto.password());
        return userRepository.save(user);
    }

    public User deleteById(Long id) {
        User user = findById(id);
        authenticationService.verifyUserAuthentication(user);
        userRepository.deleteById(id);
        return user;
    }

}
