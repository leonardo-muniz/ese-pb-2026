package com.exchange.crypto.user.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.exchange.crypto.config.ResourceNotFoundException;
import com.exchange.crypto.user.application.dto.UserHistory;
import com.exchange.crypto.user.application.dto.UserRequest;
import com.exchange.crypto.user.domain.entity.User;
import com.exchange.crypto.user.domain.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public User createUser(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }

        User newUser = new User(
                null,
                request.fullName(),
                request.email(),
                request.password(),
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );

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

        existingUser.setName(request.fullName());
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

    public List<UserHistory> getUserHistory(UUID id) {
        return userRepository.findHistoryById(id);
    }
}