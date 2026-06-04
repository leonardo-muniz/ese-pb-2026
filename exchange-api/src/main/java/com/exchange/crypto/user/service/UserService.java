package com.exchange.crypto.user.service;

import com.exchange.crypto.user.model.User;
import com.exchange.crypto.user.dto.UserRequest;
import com.exchange.crypto.user.repository.UserRepository;
import com.exchange.crypto.config.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public User createUser(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }

        User newUser = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(request.password())
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    public User updateUser(UUID id, UserRequest request) {
        User existingUser = getUserById(id);

        if (!existingUser.getEmail().equals(request.email()) &&
                userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está em uso por outro usuário.");
        }

        existingUser.setFullName(request.fullName());
        existingUser.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank()) {
            existingUser.setPassword(request.password());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(UUID id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}