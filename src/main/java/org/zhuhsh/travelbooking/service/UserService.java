package org.zhuhsh.travelbooking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zhuhsh.travelbooking.model.Role;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.agent.secret}")
    private String agentSecret;

    @Value("${security.admin.secret}")
    private String adminSecret;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String password, Role role) {
        if (!username.matches("^[a-zA-Z0-9._]{3,30}$")) {
            throw new IllegalArgumentException(
                    "Username может содержать только латинские буквы, цифры, точку и подчёркивание (3–30 символов)"
            );
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        user.setRole(role.name());

        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
}
