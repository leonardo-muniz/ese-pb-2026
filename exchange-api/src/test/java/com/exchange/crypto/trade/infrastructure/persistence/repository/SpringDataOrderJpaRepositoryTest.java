package com.exchange.crypto.trade.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.exchange.crypto.trade.domain.entity.OrderStatus;
import com.exchange.crypto.trade.domain.entity.OrderType;
import com.exchange.crypto.trade.infrastructure.persistence.entity.OrderJpaEntity;
import com.exchange.crypto.user.infrastructure.persistence.entity.UserJpaEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class SpringDataOrderJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpringDataOrderJpaRepository repository;

    @Test
    void shouldPersistAndFindOrder() {
        // 1. Arrange: Persiste o User
        UserJpaEntity user = new UserJpaEntity();
        user.setName("Leonardo Muniz");
        user.setEmail("leonardo@exemplo.com");
        user.setPassword("123456");
        user.setCreatedAt(LocalDateTime.now());
        entityManager.persist(user);

        // 2. Arrange: Configura a Order
        OrderJpaEntity order = new OrderJpaEntity();
        order.setUser(user);
        order.setAmount(new BigDecimal("50.00"));
        order.setPrice(new BigDecimal("350000.00"));
        order.setCryptoCurrency("BTC");
        order.setType(OrderType.BUY);
        order.setStatus(OrderStatus.OPEN);
        order.setCreatedAt(LocalDateTime.now());

        // 3. Act: Salva e limpa o contexto para testar a busca real no banco
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();

        // 4. Assert: Busca no repositório e valida os dados
        var foundOrder = repository.findById(order.getId());

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getAmount()).isEqualByComparingTo("50.00");
        assertThat(foundOrder.get().getUser().getEmail()).isEqualTo("leonardo@exemplo.com");
    }
}