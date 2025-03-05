package org.example.crudApplication.controller;

import org.example.crudApplication.dto.UserDto;
import org.example.crudApplication.dto.UserMapper;
import org.example.crudApplication.models.User;
import org.example.crudApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.crudApplication.config.JwtUtil;
import java.util.Collections;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = UserMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(UserMapper.toDto(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }


    @GetMapping
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.toDto(userOpt.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted");
    }
}
