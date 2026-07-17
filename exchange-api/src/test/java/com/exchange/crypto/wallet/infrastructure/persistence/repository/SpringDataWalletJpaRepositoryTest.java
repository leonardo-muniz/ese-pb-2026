package com.exchange.crypto.wallet.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.exchange.crypto.user.infrastructure.persistence.entity.UserJpaEntity;
import com.exchange.crypto.wallet.infrastructure.persistence.entity.WalletJpaEntity;

@DataJpaTest
@ActiveProfiles("test")
class WalletJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpringDataWalletJpaRepository repository;

    @Test
    void shouldPersistAndFindWallet() {
        // 1. Arrange: Persiste o dono da carteira primeiro
        UserJpaEntity user = new UserJpaEntity();
        user.setName("Leonardo Muniz");
        user.setEmail("leonardo@exemplo.com");
        user.setPassword("123456");
        user.setCreatedAt(LocalDateTime.now());
        entityManager.persist(user);

        WalletJpaEntity wallet = new WalletJpaEntity();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("BRL");

        // 2. Act
        entityManager.persist(wallet);
        entityManager.flush();
        entityManager.clear(); // Evita conflitos de lock

        // 3. Assert
        var foundWallet = repository.findById(wallet.getId());
        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getUser().getEmail()).isEqualTo("leonardo@exemplo.com");
    }
}