package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.model.dto.UserDTO;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.repository.UserRepository;
import br.com.mundim.CarRent.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static User user;
    private static UserDTO userDTO;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L).name("Bruno Mundim").email("bruno@email.com").cpf("653.189.520-97")
                .password("password").contacts(List.of("32323232"))
                .role("ROLE_USER")
                .build();
        userDTO = UserDTO.builder()
                .name("Bruno Mundim").email("bruno@email.com").cpf("653.189.520-97")
                .password("new password").contacts(List.of("32323232"))
                .build();
    }

    @Test
    public void newUserDto_shouldReturnUser() {
        User newUser = new User(userDTO);

        assertThat(newUser.getName()).isEqualTo(user.getName());
        assertThat(newUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(newUser.getCpf()).isEqualTo(user.getCpf());
        assertThat(newUser.getContacts()).isEqualTo(user.getContacts());
        assertThat(newUser.getRole()).isEqualTo(user.getRole());
    }

    @Test
    public void create_shoulReturnCreatedUser() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User createdUser = userService.create(userDTO);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    public void findById_shouldReturnFoundUser() {
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.findById(user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1L);
    }

    @Test
    public void findByEmail_shouldReturnFoundUser() {
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(user);

        User foundUser = userService.findByEmail(user.getEmail());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void findByCpf_shouldReturnFoundUser() {
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);
        when(userRepository.findByCpf(Mockito.any(String.class))).thenReturn(user);

        User foundUser = userService.findByCpf(user.getCpf());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void findAll_shouldReturnTwoUsers() {
        List<User> users = List.of(user, user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertThat(foundUsers).isNotNull();
        assertThat(foundUsers).isEqualTo(users);
    }

    @Test
    public void update_shouldReturnUpdatedUser() {
        UserDTO updateUserDTO = UserDTO.builder()
                .name("Victor Elias").email("elias@email.com").cpf("826.011.260-43")
                .password("new password").contacts(List.of("34343434"))
                .build();
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User updatedUser = userService.update(user.getId(), updateUserDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Victor Elias");
        assertThat(updatedUser.getEmail()).isEqualTo("elias@email.com");
        assertThat(updatedUser.getCpf()).isEqualTo("826.011.260-43");
        assertThat(updatedUser.getContacts()).isEqualTo(List.of("34343434"));
    }

    @Test
    public void deleteById_shouldReturnDeletedUser() {
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        User deletedUser = userService.deleteById(user.getId());

        assertThat(deletedUser).isEqualTo(user);
    }
}
