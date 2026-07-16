package com.exchange.crypto.user.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.exchange.crypto.user.application.dto.UserHistory;
import com.exchange.crypto.user.domain.entity.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void delete(User user);
    List<UserHistory> findHistoryById(UUID id);
}