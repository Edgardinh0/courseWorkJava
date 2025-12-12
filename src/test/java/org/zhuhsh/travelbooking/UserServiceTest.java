package org.zhuhsh.travelbooking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zhuhsh.travelbooking.model.Role;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.repository.UserRepository;
import org.zhuhsh.travelbooking.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final UserService userService = new UserService(userRepository, encoder);

    @Test
    void testCreateUserSuccess() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(encoder.encode("pass")).thenReturn("encoded");

        User saved = new User();
        saved.setUsername("john");
        saved.setPassword("encoded");
        saved.setRole("TRAVELER");

        when(userRepository.save(any(User.class))).thenReturn(saved);

        User user = userService.createUser("john", "pass", Role.TRAVELER);

        Assertions.assertEquals("john", user.getUsername());
        Assertions.assertEquals("encoded", user.getPassword());
        Assertions.assertEquals("TRAVELER", user.getRole());
    }



    @Test
    void testCreateUserAlreadyExists() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.createUser("john", "pass", Role.TRAVELER);
        });
    }
}
