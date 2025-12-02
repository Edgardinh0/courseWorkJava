package org.zhuhsh.travelbooking.controller;

import org.springframework.web.bind.annotation.*;
import org.zhuhsh.travelbooking.model.Role;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.service.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public List<User> getAllUsers(Principal principal) {
        String currentUsername = principal.getName();

        return userService.getAllUsers().stream()
                .filter(user -> !user.getUsername().equals(currentUsername))
                .toList();
    }


    @DeleteMapping("/admin/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
