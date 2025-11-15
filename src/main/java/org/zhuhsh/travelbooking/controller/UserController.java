package org.zhuhsh.travelbooking.controller;

import org.zhuhsh.travelbooking.model.Role;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam Role role) {
        return userService.createUser(username, password, role);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
