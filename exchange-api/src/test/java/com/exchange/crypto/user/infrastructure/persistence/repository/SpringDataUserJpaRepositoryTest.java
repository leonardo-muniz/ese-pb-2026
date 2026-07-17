package com.exchange.crypto.user.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.exchange.crypto.user.infrastructure.persistence.entity.UserJpaEntity;

@DataJpaTest
@ActiveProfiles("test")
class SpringDataUserJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpringDataUserJpaRepository repository;

    @Test
    void shouldPersistAndFindUser() {
        // Arrange
        UserJpaEntity user = new UserJpaEntity();

        user.setName("Leonardo Muniz");
        user.setEmail("leonardo@exemplo.com");
        user.setPassword("123456");
        user.setCreatedAt(LocalDateTime.now());

        // Act
        // O Hibernate vai gerar o ID automaticamente aqui
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        // Assert
        var foundUser = repository.findByEmail("leonardo@exemplo.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Leonardo Muniz");
    }
}