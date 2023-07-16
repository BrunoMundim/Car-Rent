package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    private static User user;
    private static User user2;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .name("Bruno Mundim").email("bruno@email.com").cpf("653.189.520-97")
                .password("password").contacts(List.of("32323232"))
                .build();
        user2 = User.builder()
                .name("Victor Elias").email("victor@email.com").cpf("826.011.260-43")
                .password("password").contacts(List.of("34343434"))
                .build();
    }

    @Test
    public void create_shouldReturnCreatedUser() {
        User createdUser = userRepository.save(user);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isGreaterThan(0);
    }

    @Test
    public void findById_shouldReturnFoundUser() {
        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).orElse(null);

        assertThat(foundUser).isNotNull();
    }

    @Test
    public void findByCpf_shouldReturnFoundUser() {
        userRepository.save(user);

        User foundUser = userRepository.findByCpf(user.getCpf());

        assertThat(foundUser).isNotNull();
    }

    @Test
    public void findByEmail_shouldReturnFoundUser() {
        userRepository.save(user);

        User foundUser = userRepository.findByEmail(user.getEmail());

        assertThat(foundUser).isNotNull();
    }

    @Test
    public void findAll_shouldReturnTwoUsers() {
        userRepository.save(user);
        userRepository.save(user2);

        List<User> foundUsers = userRepository.findAll();

        assertThat(foundUsers).isNotNull();
        assertThat(foundUsers.size()).isEqualTo(2);
    }

    @Test
    public void deleteById_shouldDeleteUser() {
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        assertThat(foundUser).isNull();
    }

}
