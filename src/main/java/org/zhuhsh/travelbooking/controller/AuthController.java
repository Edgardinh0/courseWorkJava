package org.zhuhsh.travelbooking.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.model.Role;
import org.zhuhsh.travelbooking.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> payload,
            HttpSession session
    ) {
        String username = payload.get("username");
        String password = payload.get("password");
        String captcha = payload.get("captcha");

        String expected = (String) session.getAttribute("CAPTCHA");

        if (expected == null || captcha == null ||
                !expected.equalsIgnoreCase(captcha)) {
            return ResponseEntity.badRequest().body("Неверная капча");
        }

        session.removeAttribute("CAPTCHA");

        try {
            User user = userService.login(username, password);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> payload,
            HttpSession session
    ) {
        String username = payload.get("username");
        String password = payload.get("password");
        String roleStr = payload.get("role");
        String captcha = payload.get("captcha");

        String expected = (String) session.getAttribute("CAPTCHA");

        if (expected == null || captcha == null ||
                !expected.equalsIgnoreCase(captcha)) {
            return ResponseEntity.badRequest().body("Неверная капча");
        }

        session.removeAttribute("CAPTCHA");

        try {
            Role role = Role.valueOf(roleStr);
            User user = userService.createUser(username, password, role);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}