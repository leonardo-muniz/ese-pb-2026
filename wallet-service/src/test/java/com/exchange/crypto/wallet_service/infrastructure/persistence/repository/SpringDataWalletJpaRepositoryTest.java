package com.exchange.crypto.wallet_service.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.exchange.crypto.wallet_service.infrastructure.persistence.entity.WalletJpaEntity;

@DataJpaTest
@ActiveProfiles("test")
class WalletJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpringDataWalletJpaRepository repository;

    @Test
    void shouldPersistAndFindWallet() {
        WalletJpaEntity wallet = new WalletJpaEntity();
        wallet.setUserId(java.util.UUID.randomUUID());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("BRL");

        // 2. Act
        entityManager.persist(wallet);
        entityManager.flush();
        entityManager.clear(); // Evita conflitos de lock

        // 3. Assert
        var foundWallet = repository.findById(wallet.getId());
        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getUserId()).isEqualTo(wallet.getUserId());
    }
}